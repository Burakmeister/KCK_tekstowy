package src.main.java;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import src.main.java.obstacles.*;
import src.main.java.pieces.Rocket;
import src.main.resources.Arts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Game{
    static public boolean paused;
    static public boolean gameover;

    private final TextColor gray = TextColor.Factory.fromString("white");
    private final TextColor white = new TextColor.RGB(255,255,255);
    private final TextColor black = new TextColor.RGB(0,0,0);

    private final TextGraphics tg;
    private final Screen screen;

    private final String []base = Arts.BASE.art;
    private final String [][]rocketArt = {Arts.ROCKET_BASE.art, Arts.ROCKET_FAST.art};

    private final MapGenerator mg = MapGenerator.getInstance();
    private static final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private static final ArrayList<Bullet> rocketBullets = new ArrayList<>();
    private final ArrayList<Bullet> ufoBullets = new ArrayList<>();
    private static int toNextBullet = 15;   //0 - has bullet

    private final Rocket rocket = Rocket.getInstance();
    public static final int MAX_HEIGHT = 2000;

    public Game(Screen screen) throws IOException, InterruptedException {
        gameover = false;
        paused = false;
        this.rocket.setColumn(base[0].length()-rocketArt[0][0].length()-37);
        this.screen = screen;
        this.tg = screen.newTextGraphics();
        this.tg.setBackgroundColor(black);
        this.tg.setForegroundColor(white);
        this.screen.clear();
        this.printBase(0);
        this.printRocket(Arts.ROCKET_BASE, this.rocket.getColumn());
        screen.refresh();
        while(!gameover) {
            KeyStroke key = screen.readInput();
            switch (key.getKeyType()) {
                case ArrowUp -> this.start();
                case Tab -> {
                    this.shop();
                    this.tg.setBackgroundColor(black);
                    this.tg.setForegroundColor(white);
                    this.screen.clear();
                    this.printBase(0);
                    this.printRocket(Arts.ROCKET_BASE, this.rocket.getColumn());
                }
                case Escape -> {
                    return;
                }
            }
        }
    }

    private void printRocket(Arts rocketType, int column) throws IOException {
        int i;
        switch(rocketType){
            case ROCKET_BASE ->{
                i = Menu.frameHeight-(rocketArt[0].length+1);
                for (String s: rocketArt[0]) {
                    this.tg.putString(column,i,s);
                    i++;
                }
            }
            case ROCKET_FAST -> {
                i = Menu.frameHeight-(rocketArt[1].length+1);
                for (String s: rocketArt[1]) {
                    this.tg.putString(column,i,s);
                    i++;
                }
            }
        }
        screen.refresh();
    }

    private void printBullets(){
            for (Bullet b : rocketBullets) {
                b.setX(b.getX() - 2);
            }
            for (int i = 0; i < rocketBullets.size(); i++) {
                if (rocketBullets.get(i).getY() < 0) {
                    rocketBullets.remove(i);
                    i--;
                }
            }
        for (Bullet b : ufoBullets) {
            b.setX(b.getX() + 2);
        }
        for (int i = 0; i < ufoBullets.size(); i++) {
            if (ufoBullets.get(i).getX() > Menu.frameHeight || ufoBullets.get(i).isToRemove()) {
                ufoBullets.remove(i);
                i--;
            }
        }for (int i = 0; i < rocketBullets.size(); i++) {
            if (rocketBullets.get(i).getX() < 0 || rocketBullets.get(i).isToRemove()) {
                rocketBullets.remove(i);
                i--;
            }
        }
        for (Bullet b : rocketBullets) {
            tg.setCharacter(b.getY(), b.getX(), b.getArt());
        }for (Bullet b : ufoBullets) {
            if(b.getY()>=rocket.getColumn() &&
                    b.getY()<=rocket.getColumn()+Arts.ROCKET_FAST.art[0].length() &&
                    b.getX()>Menu.frameHeight-Arts.ROCKET_FAST.art.length-2 &&
                    b.getX()<Menu.frameHeight-2)
            {
                    b.setToRemove(true);
                    rocket.setHealthStatus(rocket.getHealthStatus()-0.12f + 0.01f*(float)rocket.getArmor());
            }
            tg.setCharacter(b.getY(), b.getX(), b.getArt());
        }
    }

    private void printMap() {
        int j=Menu.frameHeight-1;
        for(String str: mg.getFrame()){
            tg.putString(0,j,str);
            j--;
        }
        int n = mg.getN();
        for (Obstacle o: obstacles){
            if(o.getY()<n) {
                if(!o.isENED())
                {
                    o.n();
                    o.setY(o.getY()-n);
                }
                if (o instanceof Bird) {
                    o.setX(o.getX()+o.getMoving());
                    tg.putString((int)o.getX(), o.getY(), Arts.BIRD.art[0]);
                } else if (o instanceof Cloud c) {
                    for (int i = c.getArt().art.length-1; i>=0; i--)
                        tg.putString((int)c.getX(), c.getY()+i, c.getArt().art[i]);
                } else if (o instanceof Plane c) {
                    o.setX(o.getX()+o.getMoving());
                    for (int i = c.getArt().art.length-1; i>=0; i--)
                        tg.putString((int)c.getX(), c.getY()+i, c.getArt().art[i]);
                } else if (o instanceof Ufo u) {
                    o.setX(o.getX()+o.getMoving());
                    for (int i = Arts.UFO.art.length-1; i>=0; i--) {
                        tg.putString((int)u.getX(), u.getY()+i, Arts.UFO.art[i]);
                    }
                    shotUfo(o);
                }
                o.setY(o.getY()+1);
                if(MapGenerator.checkColisions(rocket,o)){
                    o.setToRemove();
                }
            }
            if(o.isTransparent())
                for (Bullet b : rocketBullets) {
                    if (    b.getY() >= o.getX() &&
                            b.getY() <= o.getX() + o.getArt().art[0].length() &&
                            b.getX() >= o.getY() &&
                            b.getX() <= o.getY() + o.getArt().art.length) {
                        b.setToRemove(true);
                        o.setHealthPoints(o.getHealthPoints()-rocket.getFirepower());
                        if(o.getHealthPoints() == 0){
                            o.setToRemove();
                            if(o instanceof Ufo){
                                rocket.setAccountBalance(rocket.getAccountBalance()+100);
                            }if(o instanceof Plane){
                                rocket.setAccountBalance(rocket.getAccountBalance()+20);
                            }
                        }

                    }
                }
            if(o.getX()>Menu.frameWidth || o.getX()<0 || (o.getY()>Menu.frameHeight && o.isENED())){
                o.setToRemove();
            }

        }
        for(int m=0; m<obstacles.size(); m++){
            if(obstacles.get(m).isToRemove()){
                obstacles.remove(m);
                m--;
            }
        }
    }

    private void printBase(int v) {
        int i = Menu.frameHeight-base.length+v;
        for (String s: base) {
            this.tg.putString(0,i,s);
            i++;
        }
        String []sky = mg.getCurFrame();
        int n=v;
        for(int j=0; j<v; j++){
            this.tg.putString(0,j,sky[n]);
            n--;
        }
    }

    private void start() throws IOException, InterruptedException {
        gameover = false;
        paused = false;
        mg.newMap();
        MoveHadler mh = new MoveHadler(screen, rocket);
        mh.start();
        int v=0;
        while(!gameover) {
            if(!paused) {
                printRocket(Arts.ROCKET_FAST, this.rocket.getColumn());
                Thread.sleep(175);
                if (v < base.length) {
                    printBase(v);
                } else {
                    printMap();
                }
                this.printStatus(v);
                this.printBullets();
                this.rocket.setHeight((float) v / (float) MAX_HEIGHT);
                this.rocket.setOilStatus((float) (this.rocket.getOilStatus() - 0.005/rocket.getTankCapacity()));
                v++;
                screen.refresh();
                if(rocket.getHealthStatus()<=0 || rocket.getOilStatus()<=0 || v == MAX_HEIGHT){
                    gameover=true;
                }
                if(Game.getToNextBullet()>=0)
                    Game.setToNextBullet(Game.getToNextBullet()- rocket.getRateOfFire());
            }else{
                mh.setFlag();
                this.pause();
                mh = new MoveHadler(screen, rocket);
                mh.start();
            }
        }
        mh.setFlag();
        if(v==MAX_HEIGHT){
            Rocket.newInstance();
        }
        int points = 0;
        if(v > rocket.getRecord()){
            points+=rocket.getRecord()/2;
            points+=v-rocket.getRecord();
            rocket.setRecord(v);
        }else{
            points+=v/2;
        }
        rocket.setAccountBalance(rocket.getAccountBalance() + points);
        rocket.setHeight(0);
        rocket.setOilStatus(1);
        rocket.setHealthStatus(1);
        rocket.setSpeed(0);
    }

    private void pause() throws IOException {
        tg.setBackgroundColor(white);
        tg.setForegroundColor(black);
        TerminalSize ts = new TerminalSize(2*Menu.frameWidthMenu, Menu.frameWidthMenu);
        TerminalPosition tp = new TerminalPosition(Menu.frameWidth/2-Menu.frameWidthMenu, Menu.frameHeight/2-Menu.frameWidthMenu/2);
        tg.fillRectangle(tp, ts, ' ');
        Menu.paintBorder(tg, tp, ts);
        int numOption = 0;
        String []options = new String []{"Wznów", "Restart"};
        screen.refresh();
        while(true){
            Menu.paintMenuOptions(tg, options, numOption, Menu.frameWidthMenu,
                    Menu.frameHeight/2-Menu.frameWidthMenu/2, 0, white, gray);
            KeyStroke ks = screen.readInput();
            switch (ks.getKeyType()){
                case Escape ->{
                    tg.setBackgroundColor(black);
                    tg.setForegroundColor(white);
                    paused = false;
                    return;
                }
                case ArrowUp -> numOption--;
                case ArrowDown -> numOption++;
                case Enter -> {
                    switch (numOption) {
                        case 0 -> {
                            tg.setBackgroundColor(black);
                            tg.setForegroundColor(white);
                            paused = false;
                            return;
                        }
                        case 1 -> {
                            gameover = true;
                            return;
                        }
                    }
                }
            }
            if(numOption<0){
                numOption = options.length-1;
            }else if(numOption>=options.length){
                numOption=0;
            }
        }
    }
    public static int getToNextBullet(){
        return toNextBullet;
    }

    public static void setToNextBullet(int s){
        Game.toNextBullet=s;
    }

    private void shop() throws IOException {
        TerminalPosition tp = new TerminalPosition(Menu.frameWidth-Menu.frameWidthMenu, 0);
        TerminalSize ts = new TerminalSize(Menu.frameWidthMenu, Menu.frameHeight);
        tg.drawRectangle(tp, ts, ' ');
        Menu.paintBorder(tg, tp, ts);
        int numOption = 0;
        String[] shopOpts = {"Bak paliwa", "Siła ognia", "Pancerz", "Szybkostrzelność"};
        while(true){
            Menu.paintMenuOptions(tg, shopOpts, numOption, Menu.frameHeight, -2, (Menu.frameWidth-Menu.frameWidthMenu)/2,  black, gray);
            tg.setForegroundColor(white);
            tg.setBackgroundColor(black);
            for(int n=1; n<=shopOpts.length; n++)
                for(int j=0; j<10; j++){
                    tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 5 + j,
                            n*Menu.frameHeight/5), Symbols.BLOCK_MIDDLE);
            }
            for(int j = 0; j< rocket.getTankCapacity(); j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 5 + j,
                        Menu.frameHeight/5), Symbols.BLOCK_DENSE);
            }
            for(int j = 0; j< rocket.getFirepower(); j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 5 + j,
                        2*Menu.frameHeight/5), Symbols.BLOCK_DENSE);
            }
            for(int j = 0; j< rocket.getArmor(); j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 5 + j,
                        3*Menu.frameHeight/5), Symbols.BLOCK_DENSE);
            }
            for(int j = 0; j< rocket.getRateOfFire(); j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 5 + j,
                        4*Menu.frameHeight/5), Symbols.BLOCK_DENSE);
            }
            String tmp = " ".repeat(Math.max(0, String.valueOf(rocket.getAccountBalance()).length() + 3));
            tg.putString(new TerminalPosition((Menu.frameWidth - Menu.frameWidthMenu/2 - String.valueOf(rocket.getAccountBalance()).length()/3+2),
                    (Menu.frameHeight- 2)), tmp);
            tg.putString(new TerminalPosition((Menu.frameWidth - Menu.frameWidthMenu/2 - String.valueOf(rocket.getAccountBalance()).length()/3+3),
                    (Menu.frameHeight- 2)), rocket.getAccountBalance() +"$");
            screen.refresh();
            KeyStroke ks = screen.readInput();
            switch (ks.getKeyType()){
                case ArrowDown -> numOption++;
                case ArrowUp -> numOption--;
                case Tab -> {
                    return;
                }
                case Enter -> {
                    switch (numOption) {
                        case 0 -> //paliwo
                                rocket.plusOneTankCapacity();
                        case 1 -> //siła ognia
                                rocket.plusOneFirepower();
                        case 2 -> //pancerz
                                rocket.plusOneArmor();
                        case 3 -> //szybkostrzelnosc
                                rocket.plusOneRateOfFire();
                    }
                }
            }
            if(numOption<0){
                numOption = shopOpts.length-1;
            }else if(numOption >= shopOpts.length){
                numOption = 0;
            }
        }
    }

    private void printStatus(int v){
        String[] stats={"Fuel", "Health", "Height", "Speed"};
        tg.setBackgroundColor(white);
        tg.setForegroundColor(black);
        tg.fillRectangle(new TerminalPosition(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2),
                new TerminalSize(Menu.frameWidthMenu, Menu.frameWidthMenu/2), ' ');

        tg.drawRectangle(new TerminalPosition(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2),
                new TerminalSize(Menu.frameWidthMenu, Menu.frameWidthMenu/2), Symbols.SINGLE_LINE_HORIZONTAL);

        tg.drawLine(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight - 2, Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2+1, Symbols.SINGLE_LINE_VERTICAL);
        tg.drawLine(Menu.frameWidth-1, Menu.frameHeight - 2, Menu.frameWidth-1, Menu.frameHeight-Menu.frameWidthMenu/2+1, Symbols.SINGLE_LINE_VERTICAL);
        tg.setCharacter(Menu.frameWidth - 1, Menu.frameHeight-Menu.frameWidthMenu/2, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
        tg.setCharacter(Menu.frameWidth - 1, Menu.frameHeight - 1, Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER);
        tg.setCharacter(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight - 1, Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER);
        tg.setCharacter(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
        for(int i=0; i<stats.length; i++) {
            tg.putString(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu+1,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * (i + 1) / (stats.length + 1))-1), stats[i]);
        }

        tg.putString(new TerminalPosition((Menu.frameWidth - Menu.frameWidthMenu/2 - String.valueOf(rocket.getAccountBalance()).length()/3+2),
                (Menu.frameHeight- Menu.frameWidthMenu/2 + 1)), "Account");
        tg.setBackgroundColor(black);
        tg.setForegroundColor(white);

        for(int i=0; i<stats.length-1; i++) {
            for(int j=0 ; j<Menu.frameWidthMenu-2; j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                        Menu.frameHeight- (Menu.frameWidthMenu/2 * (i + 1) / (stats.length + 1))) , Symbols.BLOCK_MIDDLE);
            }
        }


        int fuel = (int) ((rocket.getOilStatus())*(Menu.frameWidthMenu-2));
        for(int j=0 ; j<fuel; j++){
            tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                    Menu.frameHeight- (Menu.frameWidthMenu / 2 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }

        tg.putString(new TerminalPosition((Menu.frameWidth - Menu.frameWidthMenu/2 - String.valueOf(rocket.getAccountBalance()).length()/3+2),
                (Menu.frameHeight- Menu.frameWidthMenu/2 + 2)), rocket.getAccountBalance() +"$");

        int damages = (int) ((rocket.getHealthStatus())*(Menu.frameWidthMenu-2));
        for(int j=0 ; j<damages; j++){
            tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * 2 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }

        int height = (int) ((rocket.getHeight())*(Menu.frameWidthMenu-2));
        for(int j=0 ; j<height; j++){
            tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * 3 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }

        if(v < base.length){
            rocket.setSpeed(rocket.getSpeed()+3);
        }else{
            Random rand = new Random();
            rocket.setSpeed(rand.nextInt(3)+115);
        }
        tg.putString(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * stats.length / (stats.length + 1))) , rocket.getSpeed() + "m/s");

    }
    public static ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public static void shotRocket(Rocket rocket){
        rocketBullets.add(new Bullet(Menu.frameHeight - Arts.ROCKET_FAST.art.length - 2,
                rocket.getColumn()+Arts.ROCKET_FAST.art[0].length()/2));
    }

    private void shotUfo(Obstacle o){
        Random rand = new Random();
        if (rand.nextInt(3) == 0) {
            this.ufoBullets.add(new Bullet(o.getY() + o.getArt().art.length / 2, (int) o.getX() + o.getArt().art[0].length() / 2));
        }
    }
}

class MoveHadler extends Thread{
    private final Screen screen;
    private final Rocket rocket;
    private boolean flag;
    public MoveHadler(Screen screen, Rocket rocket){
        this.screen = screen;
        this.rocket = rocket;
        this.flag = false;
        setDaemon(true);
    }
    @Override
    public void run() {
        while (!Game.gameover && !Game.paused) {
            try {
                KeyStroke key = screen.readInput();
                switch (key.getKeyType()) {
                    case ArrowLeft -> {
                        if(rocket.getColumn()>=0)
                            rocket.setColumn(rocket.getColumn() - 1);
                    }
                    case ArrowRight -> {
                            if(rocket.getColumn() < Menu.frameWidth-Menu.frameWidthMenu*1.5+1)
                            rocket.setColumn(rocket.getColumn() + 1);
                    }
                    case ArrowUp -> {
                        if(Game.getToNextBullet()<=0){
                            Game.shotRocket(rocket);
                            Game.setToNextBullet(15);
                        }
                    }
                    case Escape -> Game.paused = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(flag){
                break;
            }
        }
    }
    public void setFlag(){
        this.flag = true;
    }
}

class Bullet{
    private int x;
    private final int y;
    private boolean toRemove = false;
    public Bullet(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public char getArt() {
        return '|';
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }
}
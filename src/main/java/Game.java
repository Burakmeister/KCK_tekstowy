package src.main.java;

import src.main.java.pieces.Rocket;
import src.main.resources.Arts;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;


public class Game{
    static public boolean paused = false;
    static public boolean gameover = false;

    private TextColor lighterBlue = Menu.lighterBlue,
            lightBlue = Menu.lightBlue,
            blue  = new TextColor.RGB(50,50,255),
            darkBlue = new TextColor.RGB(20,20,100),
            gray = TextColor.Factory.fromString("white"),
            white = new TextColor.RGB(255,255,255),
            black = new TextColor.RGB(0,0,0);

    private TextGraphics tg;
    private Screen screen;

    private String []base = Arts.BASE.art;
    private String [][]rocketArt = {Arts.ROCKET_BASE.art, Arts.ROCKET_FAST.art};

    private MapGenerator mg = MapGenerator.getInstance();
    private Rocket rocket = Rocket.getInstance();
    public static final int MAX_HEIGHT = 2000;

    public Game(Screen screen) throws IOException, InterruptedException {
        this.rocket.setColumn(base[0].length()-rocketArt[0][0].length()-37);
        this.screen = screen;
        this.tg = screen.newTextGraphics();
        this.tg.setBackgroundColor(black);
        this.tg.setForegroundColor(white);
        this.screen.clear();
        this.printBase(0);
        this.printRocket(Arts.ROCKET_BASE, this.rocket.getColumn());
        while(!gameover) {
            KeyStroke key = screen.readInput();
            switch (key.getKeyType()) {
                case ArrowUp -> {
                    this.start();
                    break;
                }
                case Tab -> {
                    this.shop();
                    this.tg.setBackgroundColor(black);
                    this.tg.setForegroundColor(white);
                    this.screen.clear();
                    this.printBase(0);
                    this.printRocket(Arts.ROCKET_BASE, this.rocket.getColumn());
                    break;
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

    private void printMap() throws IOException {
        int i=Menu.frameHeight-1;
        for(String str: mg.getFrame()){
            tg.putString(0,i,str);
            i--;
        }
        screen.refresh();
    }

    private void printBase(int v) throws IOException {
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
        screen.refresh();
    }

    private void start() throws IOException, InterruptedException {
        gameover = false;
        paused = false;
        mg.newMap();
        new MoveHadler(screen, rocket).start();
        int v=0;
        while(!gameover) {
            if(!paused) {
                printRocket(Arts.ROCKET_FAST, this.rocket.getColumn());
                Thread.sleep(150);
                if (v < base.length) {
                    printBase(v);
                } else {
                    printMap();
                }
                this.printStatus();
                this.rocket.setHeight((float) v / (float) MAX_HEIGHT);
                this.rocket.setOilStatus((float) (this.rocket.getOilStatus() - 0.005));
                v++;
            }else{
                this.pause();
                new MoveHadler(screen, rocket).start();
            }
        }
    }

    private void pause() throws IOException, InterruptedException {
        tg.setBackgroundColor(white);
        tg.setForegroundColor(black);
        TerminalSize ts = new TerminalSize(2*Menu.frameWidthMenu, Menu.frameWidthMenu);
        TerminalPosition tp = new TerminalPosition(Menu.frameWidth/2-Menu.frameWidthMenu, Menu.frameHeight/2-Menu.frameWidthMenu/2);
        tg.fillRectangle(tp, ts, ' ');
        Menu.paintBorder(tg, tp, ts);
        int numOption = 0;
        String []options = new String []{"Wznów", "Zapisz", "Menu głowne"};
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
                case ArrowUp -> {
                    numOption--;
                }
                case ArrowDown -> {
                    numOption++;
                }
                case Enter -> {
                    switch (numOption){
                        case 0:{
                            tg.setBackgroundColor(black);
                            tg.setForegroundColor(white);
                            paused = false;
                            return;
                        }
                        case 1:
//                            save()
                            break;
                        case 2:{
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

    private void shop() throws IOException {
        TerminalPosition tp = new TerminalPosition(Menu.frameWidth-Menu.frameWidthMenu, 0);
        TerminalSize ts = new TerminalSize(Menu.frameWidthMenu, Menu.frameHeight);
        tg.drawRectangle(tp, ts, ' ');
        Menu.paintBorder(tg, tp, ts);
        int numOption = 0;
        String shopOpts[] = {"Bak paliwa", "Siła ognia", "Pancerz", "Szybkostrzelność", "Zestaw naprawczy"};
        while(true){
            Menu.paintMenuOptions(tg, shopOpts, numOption, 2*Menu.frameHeight/3, 0, (Menu.frameWidth-Menu.frameWidthMenu)/2,  black, gray);
            for(int j = 0; j< rocket.getTankCapacity(); j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                        5), Symbols.BLOCK_DENSE);       //hardcoding
            }
            screen.refresh();
            KeyStroke ks = screen.readInput();
            switch (ks.getKeyType()){
                case ArrowDown -> {
                    numOption++;
                }
                case ArrowUp -> {
                    numOption--;
                }
                case Tab -> {
                    return;
                }
                case Enter -> {
                    switch (numOption){
                        case 0:{    //paliwo
                            rocket.plusOneTankCapacity();
                        }
                        case 1:{    //siła ognia
                            rocket.plusOneFirepower();
                        }
                        case 2:{    //pancerz
                            rocket.plusOneArmor();
                        }
                        case 3: {   //szybkostrzelnosc
                            rocket.plusOneRateOfFire();
                        }
                        case 4: {   //zestaw naprawczy
                            rocket.plusOneRepairKitEfficacy();
                        }
                    }
                }
            }
            if(numOption<0){
                numOption = shopOpts.length-1;
            }else if(numOption>=shopOpts.length){
                numOption=0;
            }
        }
    }

    private void printStatus(){
        String[] stats={"Fuel", "Booster", "Height", "Speed"};
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
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * 1 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }

        int height = (int) ((rocket.getHeight())*(Menu.frameWidthMenu-2));
        for(int j=0 ; j<height; j++){
            tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * 3 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }
                    tg.putString(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * stats.length / (stats.length + 1))) , String.valueOf(rocket.getSpeed()) + "m/s");
    }
}

class MoveHadler extends Thread{
    private Screen screen;
    private Rocket rocket;
    public MoveHadler(Screen screen, Rocket rocket){
        this.screen = screen;
        this.rocket = rocket;
        setDaemon(true);
    }
    @Override
    public void run() {
        while (!Game.gameover && !Game.paused) {
            try {
                KeyStroke key = screen.readInput();
                switch (key.getKeyType()) {
                    case ArrowLeft -> {
                        rocket.setColumn(rocket.getColumn() - 1);
                        break;
                    }
                    case ArrowRight -> {
                        rocket.setColumn(rocket.getColumn() + 1);
                        break;
                    }
                    case Escape -> {
                        Game.paused = true;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
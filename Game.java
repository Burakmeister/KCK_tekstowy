import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.ArrayList;


public class Game{
    private boolean paused = false;
    private boolean gameover = false;

    private TextGraphics tg;
    private Screen screen;

    private String []base = Arts.BASE.art;
    private String [][]rocket = {Arts.ROCKET_BASE.art, Arts.ROCKET_FAST.art};

    private MapGenerator mg = MapGenerator.getInstance();
    private Account acc;
    public static final int MAX_HEIGHT = 2000;

    public Game(Screen screen) throws IOException, InterruptedException {
        this.acc = new Account();
        this.acc.getRocket().setColumn(base[0].length()-rocket[0][0].length()-7);
        this.screen = screen;
        this.tg = screen.newTextGraphics();
        this.screen.clear();
        this.printBase(0);
        this.printRocket(Arts.ROCKET_BASE, this.acc.getRocket().getColumn());
        KeyStroke key = screen.readInput();
        switch (key.getKeyType()){
            case ArrowUp -> {
                this.start();
                break;
            }
            case ArrowDown -> {
//              this.shop();
                break;
            }
        }
    }

    private void printRocket(Arts rocketType, int column) throws IOException {
        int i;
        switch(rocketType){
            case ROCKET_BASE ->{
                i = Menu.frameHeight-(rocket[0].length+1);
                for (String s: rocket[0]) {
                    this.tg.putString(column,i,s);
                    i++;
                }
            }
            case ROCKET_FAST -> {
                i = Menu.frameHeight-(rocket[1].length+1);
                for (String s: rocket[1]) {
                    this.tg.putString(column,i,s);
                    i++;
                }
            }
        }
        screen.refresh();
    }

    private void printMap(String []map) throws IOException {
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
        screen.refresh();
    }

    private void start() throws IOException, InterruptedException {
        gameover = false;
        paused = false;
        String[] map = mg.newMap();
        Thread action = new Thread(() -> {
            while (!gameover) {
                try {
                    KeyStroke key = screen.readInput();
                    switch (key.getKeyType()) {
                        case ArrowLeft -> {
                            if(!paused) {
                                acc.getRocket().setColumn(acc.getRocket().getColumn() - 1);
                            }
                            break;
                        }
                        case ArrowRight -> {
                            if(!paused) {
                                acc.getRocket().setColumn(acc.getRocket().getColumn() + 1);
                            }
                            break;
                        }
                        case Escape -> {
//                            pause();
//                            break;
                            System.exit(0);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        action.start();
        int v=0;
        while(true) {
            if(!paused) {
                printRocket(Arts.ROCKET_FAST, this.acc.getRocket().getColumn());
                Thread.sleep(150);
                if (v < base.length) {
                    printBase(v);
                } else {
                    printMap(map);
                }
                this.printStatus();
                this.acc.getRocket().setHeight((float) v / (float) MAX_HEIGHT);
                this.acc.getRocket().setOilStatus((float) (this.acc.getRocket().getOilStatus() - 0.005));
                v++;
            }if(gameover){
                action = null;
                break;
            }
        }
    }

    private void pause() throws IOException {
        paused=!paused;
        tg.setBackgroundColor(TextColor.Factory.fromString("WHITE"));
        tg.setForegroundColor(TextColor.Factory.fromString("BLACK"));
        tg.fillRectangle(new TerminalPosition( Menu.frameHeight/2-Menu.frameWidthMenu/2, Menu.frameWidth/2-Menu.frameWidthMenu/2),new TerminalSize(1,1), ' ');

//        tg.drawRectangle(new TerminalPosition(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2),
//                new TerminalSize(Menu.frameWidthMenu, Menu.frameWidthMenu/2), Symbols.SINGLE_LINE_HORIZONTAL);
//
//        tg.drawLine(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight - 2, Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2+1, Symbols.SINGLE_LINE_VERTICAL);
//        tg.drawLine(Menu.frameWidth-1, Menu.frameHeight - 2, Menu.frameWidth-1, Menu.frameHeight-Menu.frameWidthMenu/2+1, Symbols.SINGLE_LINE_VERTICAL);
//        tg.setCharacter(Menu.frameWidth - 1, Menu.frameHeight-Menu.frameWidthMenu/2, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
//        tg.setCharacter(Menu.frameWidth - 1, Menu.frameHeight - 1, Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER);
//        tg.setCharacter(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight - 1, Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER);
//        tg.setCharacter(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight-Menu.frameWidthMenu/2, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
        screen.refresh();
    }

    private void shop(){
        tg.drawRectangle(new TerminalPosition(Menu.frameWidth-Menu.frameWidthMenu, 0),
                new TerminalSize(Menu.frameWidthMenu, Menu.frameHeight), Symbols.SINGLE_LINE_HORIZONTAL);
        tg.drawLine(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight - 2, Menu.frameWidth-Menu.frameWidthMenu, 1, Symbols.SINGLE_LINE_VERTICAL);
        tg.drawLine(Menu.frameWidth-1, Menu.frameHeight - 2, Menu.frameWidth-1, 1, Symbols.SINGLE_LINE_VERTICAL);
        tg.setCharacter(Menu.frameWidth - 1, 0, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
        tg.setCharacter(Menu.frameWidth - 1, Menu.frameHeight - 1, Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER);
        tg.setCharacter(Menu.frameWidth-Menu.frameWidthMenu, Menu.frameHeight - 1, Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER);
        tg.setCharacter(Menu.frameWidth-Menu.frameWidthMenu, 0, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
    }

    private void printStatus(){
        String[] stats={"Fuel", "Booster", "Height", "Speed"};
        tg.setBackgroundColor(TextColor.Factory.fromString("WHITE"));
        tg.setForegroundColor(TextColor.Factory.fromString("BLACK"));
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
        tg.setBackgroundColor(TextColor.Factory.fromString("BLACK"));
        tg.setForegroundColor(TextColor.Factory.fromString("WHITE"));
        for(int i=0; i<stats.length-1; i++) {
            for(int j=0 ; j<Menu.frameWidthMenu-2; j++){
                tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                        Menu.frameHeight- (Menu.frameWidthMenu/2 * (i + 1) / (stats.length + 1))) , Symbols.BLOCK_MIDDLE);
            }
        }


        int fuel = (int) ((acc.getRocket().getOilStatus())*(Menu.frameWidthMenu-2));
        for(int j=0 ; j<fuel; j++){
            tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * 1 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }

        int height = (int) ((acc.getRocket().getHeight())*(Menu.frameWidthMenu-2));
        for(int j=0 ; j<height; j++){
            tg.setCharacter(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1 + j,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * 3 / (stats.length + 1))) , Symbols.BLOCK_DENSE);
        }

        System.out.println(acc.getRocket().getOilStatus() + "   " + fuel + "\n" + acc.getRocket().getHeight() + "   " + height);


                    tg.putString(new TerminalPosition(Menu.frameWidth - Menu.frameWidthMenu + 1,
                    Menu.frameHeight- (Menu.frameWidthMenu/2 * stats.length / (stats.length + 1))) , String.valueOf(acc.getRocket().getSpeed()) + "m/s");
    }
}
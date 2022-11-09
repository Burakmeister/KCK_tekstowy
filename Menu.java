import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Menu {
    public static int frameWidth = 100;    //120  50
    public static int frameHeight = 33;    //50  50
    public static int frameWidthMenu = 20;
    public static Font font = new Font("Courier New", Font.BOLD, 25);

    private TextColor lighterBlue = new TextColor.RGB(150,150,255),
            lightBlue = new TextColor.RGB(100,100,255);

    private static DefaultTerminalFactory terminal;
    private static Screen screen;

    private TextGraphics tg;


    public Menu(boolean options) throws IOException {
        screen.setCursorPosition(null);
        this.tg = screen.newTextGraphics();
        String []menuOptions = {"", "", "Start", "Opcje", "Wyjście"};
        int numOption = 2;
        this.paintMenu();
        this.paintLogo();
        while (true){
            try {
                if(options){
                    this.options();
                    options = false;
                    this.paintMenu();
                }
                this.paintMenuOptions(menuOptions, numOption);
                KeyStroke key = screen.readInput();
                switch (key.getKeyType()){
                    case ArrowUp -> numOption--;
                    case ArrowDown -> numOption++;
                    case Enter -> {
                        switch (numOption){
                            case 2:
                                new AnimatedEarth().start();
                                Thread.sleep(3100);
                                new Game(screen);
                                break;
                            case 3:
                                new AnimatedEarth().start();
                                Thread.sleep(3100);
                                this.options();
                                this.paintMenu();
                                this.paintMenuOptions(menuOptions, numOption);
                                this.paintLogo();
                                break;
                            case 4:
                                System.exit(0);
                                break;
                        }
                    }
                }
                if(numOption < 2){
                    numOption = menuOptions.length-1;
                }else if(numOption >= menuOptions.length){
                    numOption = 2;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (InterruptedException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    private void options() throws IOException {
        String []menuOptions = {"Wielkość czcionki: \""+Symbols.ARROW_LEFT+"\" lub \""+Symbols.ARROW_RIGHT+"\"", "God Mode", "Powrót"};
        int numOption = 0;
        this.paintMenu();
        while(true){
            paintMenuOptions(menuOptions, numOption);
            KeyStroke key = screen.readInput();
            switch (key.getKeyType()){
                case ArrowUp -> numOption--;
                case ArrowDown -> numOption++;
                case Enter -> {
                    switch(numOption){
                        case 0:{
                            KeyStroke k_size;
                            while(true) {
                                k_size = screen.readInput();
                                switch (k_size.getKeyType()) {
                                    case ArrowRight -> {
                                        font = new Font(font.getFontName(), font.getStyle(), font.getSize() + 1);
                                        terminal.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));
                                        screen.stopScreen();
                                        screen = terminal.createScreen();
                                        screen.startScreen();
                                        new Menu(true);
                                        return;
                                    }
                                    case ArrowLeft -> {
                                        font = new Font(font.getFontName(), font.getStyle(), font.getSize() - 1);
                                        terminal.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));
                                        screen.stopScreen();
                                        screen = terminal.createScreen();
                                        screen.startScreen();
                                        new Menu(true);
                                        return;
                                    }
                                    case Escape -> {
                                        return;
                                    }
                                }
                            }
                        }
                        case 1: break;
                        case 2: return;
                    }
                }
            }
            if(numOption < 0){
                numOption = menuOptions.length-1;
            }else if(numOption >= menuOptions.length){
                numOption = 0;
            }
        }
    }

    private void paintMenu() throws IOException {
        tg.setBackgroundColor(lighterBlue);
        tg.fillRectangle(new TerminalPosition(0, 0),
                new TerminalSize(Menu.frameWidth, Menu.frameHeight), ' ');
        tg.setForegroundColor(TextColor.Factory.fromString("BLUE"));
        Menu.paintBorder(tg, new TerminalPosition(0,0), new TerminalSize(Menu.frameWidth, Menu.frameHeight));
        screen.refresh();
    }

    private void paintMenuOptions(String []menuOptions, int numOption) throws IOException {
            for (int i = 0; i < menuOptions.length; i++) {
                tg.setBackgroundColor(lighterBlue);
                if (i == numOption) {
                    tg.setBackgroundColor(lightBlue);
                }
                tg.putString(new TerminalPosition(Menu.frameWidth / 2 - menuOptions[i].length() / 2,
                        Menu.frameHeight * (i + 1) / (menuOptions.length + 1)), menuOptions[i]);
            }
        screen.refresh();
    }

    private void paintLogo() throws IOException {
        int i=0;
        int col = Menu.frameWidth/2 - Arts.LOGO.art[1].length()/2;
        int row = Menu.frameHeight/4 - Arts.LOGO.art.length/2;
        for(String str: Arts.LOGO.art){
            tg.putString(col,i+row,str);
            i++;
        }
        screen.refresh();
    }

    public static void main(String[] args){
        SwingTerminalFontConfiguration cfg = SwingTerminalFontConfiguration.newInstance(font);
        terminal = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8)
                .setTerminalEmulatorFontConfiguration(cfg)
                .setInitialTerminalSize(new TerminalSize(Menu.frameWidth, Menu.frameHeight))
                .setTerminalEmulatorTitle("W kosmos");
        try {
            screen = terminal.createScreen();
            screen.setCursorPosition(null);
            screen.startScreen();
            new Menu(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void paintBorder(TextGraphics tg, TerminalPosition tp, TerminalSize ts){
        tg.drawRectangle(tp, ts, Symbols.DOUBLE_LINE_HORIZONTAL);
        for(int i=tp.getRow(); i<ts.getRows()+tp.getRow()-1; i++) {
            tg.setCharacter(tp.getColumn(), i, Symbols.DOUBLE_LINE_VERTICAL);
            tg.setCharacter(ts.getColumns()+tp.getColumn()-1, i, Symbols.DOUBLE_LINE_VERTICAL);
        }
        tg.setCharacter(tp, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
        tg.setCharacter(ts.getColumns()+tp.getColumn()-1, tp.getRow(), Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
        tg.setCharacter(ts.getColumns()+tp.getColumn()-1, ts.getRows()+tp.getRow()-1, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
        tg.setCharacter(tp.getColumn(), ts.getRows()+tp.getRow() - 1, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
    }

    public class AnimatedEarth extends Thread{
        public AnimatedEarth(){
            setDaemon(true);
        }
        public void run(){
            tg.setBackgroundColor(lighterBlue);
            for(int i=0; i<Menu.frameHeight; i++){
                for(int j=0; j<Menu.frameWidth; j++) {
                    tg.setCharacter(j,i,' ');
                }
            }
            int i;
            for (Earth e : Earth.values()) {
                i=2;
                for (String str : e.art) {
                    tg.putString(Menu.frameWidth/2-str.length()/2, i, str);
                    i++;
                }
                Menu.paintBorder(tg, new TerminalPosition(0,0), new TerminalSize(Menu.frameWidth, Menu.frameHeight));
                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                try {
                    screen.refresh();
                } catch (IOException ex) {ex.printStackTrace();
                }
            }
            tg.setBackgroundColor(TextColor.Factory.fromString("BLACK"));
        }
    }
}

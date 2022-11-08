import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Menu {
    public static int frameWidth = 100;    //120  50
    public static int frameHeight = 30;    //50  50
    public static int frameWidthMenu = 20;
    public static Font font = new Font("Courier New", Font.BOLD, 30);

    private TextColor lighterBlue = new TextColor.RGB(150,150,255),
            lightBlue = new TextColor.RGB(100,100,255);

    private static DefaultTerminalFactory terminal;
    private static Screen screen;


    public Menu(boolean options) throws IOException {
        screen.setCursorPosition(null);
        TextGraphics tg = screen.newTextGraphics();
        String []menuOptions = {"Start", "Opcje", "Wyjście"};
        int numOption = 0;
        this.paintMenu(tg);
        while (true){
            try {
                if(options){
                    this.options(tg);
                    options = false;
                    this.paintMenu(tg);
                }
                this.paintMenuOptions(tg, menuOptions, numOption);
                KeyStroke key = screen.readInput();
                switch (key.getKeyType()){
                    case ArrowUp -> numOption--;
                    case ArrowDown -> numOption++;
                    case Enter -> {
                        switch (numOption){
                            case 0:
                                new Game(screen);
                                break;
                            case 1:
                                this.options(tg);
                                this.paintMenu(tg);
                                this.paintMenu(tg);
                                this.paintMenuOptions(tg, menuOptions, numOption);
                                break;
                            case 2:
                                System.exit(0);
                                break;
                        }
                    }
                }
                if(numOption < 0){
                    numOption = menuOptions.length-1;
                }else if(numOption >= menuOptions.length){
                    numOption = 0;
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

    private void options(TextGraphics tg) throws IOException {
        String []menuOptions = {"Wielkość czcionki: \""+Symbols.ARROW_LEFT+"\" lub \""+Symbols.ARROW_RIGHT+"\"", "God Mode", "Powrót"};
        int numOption = 0;
        this.paintMenu(tg);
        while(true){
            paintMenuOptions(tg, menuOptions, numOption);
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
                                        font = new Font(font.getFontName(), font.getStyle(), font.getSize() + 2);
                                        terminal.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));
                                        screen.stopScreen();
                                        screen = terminal.createScreen();
                                        screen.startScreen();
                                        new Menu(true);
                                        return;
                                    }
                                    case ArrowLeft -> {
                                        font = new Font(font.getFontName(), font.getStyle(), font.getSize() - 2);
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

    private void paintMenu(TextGraphics tg) throws IOException {
        tg.setBackgroundColor(lighterBlue);
        tg.fillRectangle(new TerminalPosition(0, 0),
                new TerminalSize(Menu.frameWidth, Menu.frameHeight), ' ');
        tg.setForegroundColor(TextColor.Factory.fromString("BLUE"));
        tg.drawRectangle(new TerminalPosition(0, 0),
                new TerminalSize(Menu.frameWidth, Menu.frameHeight), Symbols.DOUBLE_LINE_HORIZONTAL);
        tg.setCharacter(0, 0, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
        tg.drawLine(0, Menu.frameHeight - 2, 0, 1, Symbols.DOUBLE_LINE_VERTICAL);
        tg.drawLine(Menu.frameWidth - 1, Menu.frameHeight - 2, Menu.frameWidth - 1, 1, Symbols.DOUBLE_LINE_VERTICAL);
        tg.setCharacter(Menu.frameWidth - 1, 0, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
        tg.setCharacter(Menu.frameWidth - 1, frameHeight - 1, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
        tg.setCharacter(0, frameHeight - 1, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
        screen.refresh();
    }

    private void paintMenuOptions(TextGraphics tg, String []menuOptions, int numOption) throws IOException {
        for(int i=0; i<menuOptions.length; i++) {
            tg.setBackgroundColor(lighterBlue);
            if (i == numOption) {
                tg.setBackgroundColor(lightBlue);
            }
            tg.putString(new TerminalPosition(Menu.frameWidth / 2 - menuOptions[i].length() / 2, Menu.frameHeight * (i + 1) / (menuOptions.length + 1)), menuOptions[i]);
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
}

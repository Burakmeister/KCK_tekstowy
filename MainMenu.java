import java.nio.charset.Charset;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.*;
import com.googlecode.lanterna.gui.Component.Alignment;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.gui.layout.VerticalLayout;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

    public class MainMenu {
        public static void test1() {
//final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
            Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF8"));
            terminal.enterPrivateMode();
            terminal.moveCursor(10, 10);
            terminal.putCharacter('W');
            terminal.putCharacter('i');
            terminal.putCharacter('t');
            terminal.putCharacter('a');
            terminal.putCharacter('m');
            terminal.putCharacter(' ');
            terminal.putCharacter('s');
            terminal.putCharacter('t');
            terminal.putCharacter('u');
            terminal.putCharacter('d');
            terminal.putCharacter('e');
            terminal.putCharacter('n');
            terminal.putCharacter('t');
            terminal.putCharacter('a');
            terminal.putCharacter('!');
            terminal.moveCursor(0, 0);
            Thread.currentThread();
            while(terminal.readInput()==null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            terminal.exitPrivateMode();
        }

        public static void test2() {
//Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF8"));
            Screen screen = TerminalFacade.createScreen();
            screen.startScreen();
            screen.putString(4, 4, "Witam studenta ponownie!", Terminal.Color.BLUE, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
//false, false, false);
            screen.refresh();
            Thread.currentThread();
            while(screen.readInput()==null){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            screen.stopScreen();
        }

        public static void test3() {
            final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
            final Window window = new Window("Sample window");
            window.setWindowSizeOverride(new TerminalSize(100,50));
            window.setSoloWindow(true);

            Panel panelHolder = new Panel("Holder panel",Orientation.VERTICAL);

            Panel panel = new Panel("Panel with a right-aligned button");

            panel.setLayoutManager(new VerticalLayout());
            Button button = new Button("Button");
            button.setAlignment(Component.Alignment.RIGHT_CENTER);
            panel.addComponent(button, LinearLayout.GROWS_HORIZONTALLY);

            Table table = new Table(6);
            table.setColumnPaddingSize(5);

            Component[] row1 = new Component[6];
            row1[0] = new Label("Field-1----");
            row1[1] = new Label("Field-2");
            row1[2] = new Label("Field-3");
            row1[3] = new Label("Field-4");
            row1[4] = new Label("Field-5");
            row1[5] = new Label("Field-6");

            table.addRow(row1);
            panel.addComponent(table);
            panelHolder.addComponent(panel);

            window.addComponent(panelHolder);
            window.addComponent(new EmptySpace());

            final Window newWindow = new Window("This window should be of the same size as the previous one");

            Button quitButton = new Button("Show next window", new Action() {

                public void doAction() {

                    newWindow.setWindowSizeOverride(new TerminalSize(130,50));
                    newWindow.setSoloWindow(true);

                    Button exitBtn = new Button("Exit", new Action() {

                        public void doAction() {
                            newWindow.close();
                            window.close();
                        }
                    });

                    exitBtn.setAlignment(Alignment.RIGHT_CENTER);

                    newWindow.addComponent(exitBtn, LinearLayout.GROWS_HORIZONTALLY);

                    guiScreen.showWindow(newWindow);
                }
            });
            quitButton.setAlignment(Component.Alignment.RIGHT_CENTER);
            window.addComponent(quitButton, LinearLayout.GROWS_HORIZONTALLY);

            guiScreen.getScreen().startScreen();
            guiScreen.showWindow(window);
            guiScreen.getScreen().stopScreen();
        }
        /**
         * @param args
         */
        public static void main(String[] args) {
            //test1();
            //test2();
            test3();
        }

    }

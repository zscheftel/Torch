package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Menu implements Serializable {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private static TETile[][] tilesets;
    String input;
    World world;
    TERenderer render;

    Menu() {
        render = new TERenderer();
        world = new World();
    }

    Menu(String str) {
        render = new TERenderer();
        world = new World();
        input = str;
    }

    public static void main(String[] args) {

        TERenderer render = new TERenderer();
        render.initialize(WIDTH, HEIGHT);
        Game g = new Game();
        //  TETile[][] basic = g.playWithInputString("n55s");
        //  TETile[][] tiles = g.playWithInputString("n5513998301767302084swwadsa");
        //  TETile[][] selit = g.playWithInputString("n7290030535105837897sddadswwwaws:q");
        //  TETile[][] selit = g.playWithInputString("ls");
        //  render.renderFrame(selit);

        Menu menu = new Menu();
        menu.menuStart();

    }

    private static void drawMenu() {

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "Torch");

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 15));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game(N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 1 / 2 * 9 / 10, "Load Game(L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 1 / 2 * 8 / 10, "Quit(Q)");

        StdDraw.show();
    }

    private static void drawWinScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "Congratulations, you won!");

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 15));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Main menu (M)");

        StdDraw.show();
    }

    private static void drawLoseScreen() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "You lose! Try again?");

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 15));
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Main menu (M)");

        StdDraw.show();
    }


    private static long newMenu() {

        String input = "";
        char temp;
        drawFramePrompt(input);

        while (true) {

            if (StdDraw.hasNextKeyTyped()) {

                temp = StdDraw.nextKeyTyped();
                if (temp == 's' || temp == 'S') {
                    break;
                } else {
                    input += temp;
                    drawFramePrompt(input);
                }
            }

        }

        return Long.parseLong(input);
    }

    public static void drawFramePrompt(String s) {


        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "TYPE A SEED (PRESS S OR s TO STOP)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    /*
            In Progress function, not used right
     */
    public TETile[][] menu() {
        return world.setUpGame(input);
    }

    public void winScreen() {

        drawWinScreen();

        while (true) {

            char choice = '1';
            if (StdDraw.hasNextKeyTyped()) {
                choice = StdDraw.nextKeyTyped();
            }

            // New Option
            if (choice == 'M' || choice == 'm') {
                menuStart();
            }

        }
    }

    public void loseScreen() {

        drawLoseScreen();

        while (true) {

            char choice = '1';
            if (StdDraw.hasNextKeyTyped()) {
                choice = StdDraw.nextKeyTyped();
            }

            // New Option
            if (choice == 'M' || choice == 'm') {
                menuStart();
            }
        }
    }

    public void menuStart() {

        drawMenu();

        while (true) {

            char choice = '1';
            if (StdDraw.hasNextKeyTyped()) {
                choice = StdDraw.nextKeyTyped();
            }

            // New Option
            if (choice == 'N' || choice == 'n') {
                long seed = newMenu();
                world.setSeed(String.valueOf(seed));
                if (world.startGame()) {
                    quit();
                }
            } else if (choice == 'Q' || choice == 'q') {
                quit();
            } else if (choice == 'L' || choice == 'l') {
                ObjectInputStream in = null;
                try {
                    in = new ObjectInputStream(new FileInputStream(new File("world.txt")));
                    world = (World) (in.readObject());
                } catch (FileNotFoundException e) {

                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //    System.out.println("play");
                if (world.playGame()) {
                    quit();
                }
            }
        }

    }

    private void quit() {
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}

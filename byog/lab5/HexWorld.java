package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    public static void addHexagon(int size, TETile[][] tetlie, int x_pos, int y_pos) {

        fillasHex(tetlie, size, x_pos, y_pos);
    }

    private static int cal_size_hex(int n) {
        return n * 2;
    }

    private static void initialize_tetile(TETile[][] tetlie) {

        // initialize tiles

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tetlie[x][y] = Tileset.NOTHING;
            }
        }
    }


    //private void drawHex(int )

    public static void fillasHex(TETile[][] tiles, int size, int x_pos, int y_pos) {

        int height = size;
        int width = size;


        for (int y = 0; y < size; y++) {

            for (int x = -(size - y - 1); x < size - y; x += 1) {
                tiles[x + x_pos][y + y_pos] = Tileset.REDFLOWER;
                tiles[x + x_pos][-y + y_pos - 1] = Tileset.REDFLOWER;
            }
        }

    }


    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tetlie = new TETile[WIDTH][HEIGHT];

        initialize_tetile(tetlie);

        addHexagon(4, tetlie, 10, 10);
        ter.renderFrame(tetlie);


    }
}

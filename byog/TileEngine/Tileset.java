package byog.TileEngine;

import java.awt.Color;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset implements Serializable {
    public static final TETile WALL = new TETile('*', Color.darkGray, Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('・', Color.gray, Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");

    public static final TETile MAGENTAFLOWER = new TETile('❀', Color.magenta, Color.black, "flower");
    public static final TETile PINKFLOWER = new TETile('❀', Color.pink, Color.black, "flower");
    public static final TETile YELLOWFLOWER = new TETile('❀', Color.yellow, Color.black, "flower");
    public static final TETile REDFLOWER = new TETile('❀', Color.red, Color.black, "flower");
    public static final TETile ORANGEFLOWER = new TETile('❀', Color.orange, Color.black, "flower");
    public static final TETile WHITEFLOWER = new TETile('❀', Color.white, Color.black, "flower");

    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    private static TETile PLAYER = new TETile('人', Color.white, Color.black, "player");
    public static TETile GOD = new TETile('神', Color.white, Color.black, "god");
    public static TETile NORMAL = new TETile('前', Color.yellow, Color.black, "normal");

    public static TETile SWIMMER = new TETile('泳', Color.white, Color.black, "swimmer");
    public static TETile SWIM = new TETile('水', Color.yellow, Color.black, "swim");

    public static TETile DIG = new TETile('地', Color.yellow, Color.black, "dig");
    public static TETile DIGGER = new TETile('掘', Color.white, Color.black, "digger");

    public static TETile TORCH = new TETile('火', Color.orange, Color.black, "torch");

    public static TETile getPlayer(){
        return PLAYER;
    }
}



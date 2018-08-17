package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private int xPos;
    private int yPos;
    private TETile under = Tileset.FLOOR;
    private TETile model;
    private int visionRadius;
    private int flowerCount;

    Player() {
        xPos = 0;
        yPos = 0;
        model = Tileset.getPlayer();
        visionRadius = 5;
        flowerCount = 0;
    }

    Player(int x, int y) {
        xPos = x;
        yPos = y;
        model = Tileset.getPlayer();
        visionRadius = 5;
        flowerCount = 0;
    }

    public int getX() {
        return xPos;
    }

    public void setX(int X) {
        xPos = X;
    }

    public int getY() {
        return yPos;
    }

    public void setY(int Y) {
        yPos = Y;
    }

    public TETile getUnder() {
        return under;
    }

    public void setUnder(TETile tile) {
        under = tile;
    }

    public TETile getModel() {
        return model;
    }

    public void setModel(TETile newModel) {
        model = newModel;
    }

    public int getVision() {
        if (visionRadius > Math.min(World.WIDTH, World.HEIGHT) / 2) {
            return Math.min(World.WIDTH / 2, World.HEIGHT / 2);
        }
        return Math.max(0, visionRadius);
    }

    public void setVision(int radius) {
        visionRadius = radius;
    }

    public int torchTile() {
        if (visionRadius == 10) {
            return 5;
        }
        return 10;
    }

    public int getFlowerCount() {
        return flowerCount;
    }

    public void setFlowerCount(int fC) {
        flowerCount = fC;
    }

}

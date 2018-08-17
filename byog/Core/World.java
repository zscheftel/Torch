package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;


public class World implements Serializable {


    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private static Random RANDOM = new Random();
    private int[] doorPosition;
    private TETile[][] tilesets;
    private Player player;
    private TERenderer renderer;

    public World() {
        player = new Player();
        renderer = new TERenderer();
        doorPosition = new int[]{0, 0};
        tilesets = new TETile[WIDTH][HEIGHT];
    }

    public World(World w) {
        setWorld(w);
    }

    public static int[][] neighbors(int xP, int yP) {
        int[] xC;
        int[] yC;
        if (xP == 0 && yP == 0) {
            xC = new int[]{xP, xP + 1, xP + 1};
            yC = new int[]{yP + 1, yP + 1, yP};
        } else if (xP == 0 && yP == HEIGHT - 1) {
            xC = new int[]{xP, xP + 1, xP + 1};
            yC = new int[]{yP - 1, yP - 1, yP};
        } else if (xP == WIDTH - 1 && yP == 0) {
            xC = new int[]{xP - 1, xP - 1, xP};
            yC = new int[]{yP, yP + 1, yP + 1};
        } else if (xP == WIDTH - 1 && yP == HEIGHT - 1) {
            xC = new int[]{xP - 1, xP - 1, xP};
            yC = new int[]{yP, yP - 1, yP - 1};
        } else if (xP == 0) {
            xC = new int[]{xP, xP + 1, xP + 1, xP + 1, xP};
            yC = new int[]{yP + 1, yP + 1, yP, yP - 1, yP - 1};
        } else if (xP == WIDTH - 1) {
            xC = new int[]{xP, xP - 1, xP - 1, xP - 1, xP};
            yC = new int[]{yP - 1, yP - 1, yP, yP + 1, yP + 1};
        } else if (yP == 0) {
            xC = new int[]{xP - 1, xP - 1, xP, xP + 1, xP + 1};
            yC = new int[]{yP, yP + 1, yP + 1, yP + 1, yP};
        } else if (yP == HEIGHT - 1) {
            xC = new int[]{xP - 1, xP - 1, xP, xP + 1, xP + 1};
            yC = new int[]{yP, yP - 1, yP - 1, yP - 1, yP};
        } else {
            xC = new int[]{xP - 1, xP - 1, xP - 1, xP, xP, xP + 1, xP + 1, xP + 1};
            yC = new int[]{yP + 1, yP, yP - 1, yP + 1, yP - 1, yP + 1, yP, yP - 1};
        }
        int[][] rT = new int[2][xC.length];
        rT[0] = xC;
        rT[1] = yC;
        return rT;
    }

    public static int randomWallGenerator(int k) {
        if (k < 4) {
            return 1;
        }
        return RandomUtils.uniform(RANDOM, 1, k - 1);
    }

    public Random getRandom() {
        return RANDOM;
    }

    public boolean walkableTile(TETile tile) {
        if (player.getModel().description().equals("god")) {
            return true;
        }
        if (tile == null || (tile.description().equals("wall")
                && !player.getModel().description().equals("digger"))
                || (tile.description().equals("locked door")
                && player.getModel().description() != "god")
                || tile.description().equals("nothing")
                || (tile.description().equals("water")
                && !player.getModel().description().equals("swimmer"))) {
            return false;
        }
        return true;
    }

    private int[] playerPos(WalkDir wallDirection, TETile[][] world, int xPos, int yPos) {
        switch (wallDirection) {
            case X_LEFT:
                world[xPos - 1][yPos] = player.getModel();
                return new int[]{xPos - 1, yPos};
            case X_RIGHT:
                world[xPos + 1][yPos] = player.getModel();
                return new int[]{xPos + 1, yPos};
            case Y_DOWN:
                world[xPos][yPos - 1] = player.getModel();
                return new int[]{xPos, yPos - 1};
            case Y_UP:
                world[xPos][yPos + 1] = player.getModel();
                return new int[]{xPos, yPos + 1};
            default:
                return new int[]{0, 0};
        }
    }

    /*
        Generate a world with random rooms, hallway
        return : 2-D Tileset
     */

    public void setWorld(World w) {
        player = w.player;
        doorPosition = w.doorPosition;
        tilesets = w.tilesets;
    }

    public void render() {

        //  renderer.initialize(WIDTH, HEIGHT);

        TETile[][] neighbors = visionSquare(player.getX(), player.getY());

        renderer.renderFrame(neighbors);
        mouseDisplay(neighbors);
    }

    public int[] getXYDoor() {
        return doorPosition;
    }

    public Player getPlayer() {
        return player;
    }
    /*
        The function will build a room with random height and width
        with one door at xCome and y-come
     */



    /*
        Generate random hallway length continue to (xCome, yCome) 
        given what direction the last hallway came
     */

    // Starting at (0, 0) and going to (width, height), iterates through the points in the grid.
    // if a neighbor of a non-flower tile is a flower tile,
    // create a wall at that point and continue iteration
    private int[] spawnWallDoorPlayer(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                int[][] tempNeighbors = neighbors(i, k);
                TETile tempTile = world[i][k];
                if (walkableTile(tempTile)) {
                    for (int j = 0; j < tempNeighbors[0].length; j++) {

                        int xPos = tempNeighbors[0][j];
                        int yPos = tempNeighbors[1][j];

                        try {
                            if (validatedPoint(xPos, yPos) && !walkableTile(world[xPos][yPos])) {
                                world[tempNeighbors[0][j]][tempNeighbors[1][j]] = Tileset.WALL;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("X : " + xPos + "Y: " + yPos);
                        }
                    }
                }
            }
        }
        int[] newPos = playerPos(createOuterWallDoor(world), world, getXYDoor()[0], getXYDoor()[1]);
        return newPos;
    }

    private WalkDir createOuterWallDoor(TETile[][] world) {
        int[] xWalls = new int[java.lang.Math.max(WIDTH, HEIGHT)];
        int[] yWalls = new int[java.lang.Math.max(WIDTH, HEIGHT)];

        int trackNumOfWalls = 0;
        boolean stopTrigger = false;
        Direction comeDirection = new Direction(RANDOM);
        switch (comeDirection.direction) {
            case X_LEFT:
                for (int i = WIDTH - 1; i > 0 && !stopTrigger; i--) {
                    for (int k = HEIGHT - 1; k > 0; k--) {
                        if (world[i][k].description().equals("wall")) {
                            stopTrigger = true;
                            xWalls[trackNumOfWalls] = i;
                            yWalls[trackNumOfWalls] = k;
                            trackNumOfWalls++;
                        }
                    }
                }
                break;
            case X_RIGHT:
                for (int i = 0; i < WIDTH - 1 && !stopTrigger; i++) {
                    for (int k = HEIGHT - 1; k > 0; k--) {
                        if (world[i][k].description().equals("wall")) {
                            stopTrigger = true;
                            xWalls[trackNumOfWalls] = i;
                            yWalls[trackNumOfWalls] = k;
                            trackNumOfWalls++;
                        }
                    }
                }
                break;
            case Y_UP:
                for (int k = 0; k < HEIGHT - 1 && !stopTrigger; k++) {
                    for (int i = 0; i < WIDTH - 1; i++) {
                        if (world[i][k].description().equals("wall")) {
                            stopTrigger = true;
                            xWalls[trackNumOfWalls] = i;
                            yWalls[trackNumOfWalls] = k;
                            trackNumOfWalls++;
                        }
                    }
                }
                break;
            case Y_DOWN:
                for (int k = HEIGHT - 1; k > 0 && !stopTrigger; k--) {
                    for (int i = 0; i < WIDTH - 1; i++) {
                        if (world[i][k].description().equals("wall")) {
                            stopTrigger = true;
                            xWalls[trackNumOfWalls] = i;
                            yWalls[trackNumOfWalls] = k;
                            trackNumOfWalls++;
                        }
                    }
                }
                break;
            default:
                break;
        }
        boolean foundWall = false;
        int randomWall = 0;
        while (!foundWall) {
            int nullTrigger = 0;
            randomWall = randomWallGenerator(trackNumOfWalls);
            int[][] tempN = neighbors(xWalls[randomWall], yWalls[randomWall]);
            for (int i = 0; i < tempN[0].length; i++) {

                int x = tempN[0][i];
                int y = tempN[1][i];

                if (validatedPoint(x, y) && Tileset.NOTHING.equals(world[x][y])) {
                    nullTrigger++;
                }
            }
            if (nullTrigger < 4) {
                foundWall = true;
            }
        }
        this.doorPosition = new int[]{xWalls[randomWall], yWalls[randomWall]};
        world[xWalls[randomWall]][yWalls[randomWall]] = Tileset.LOCKED_DOOR;
        return comeDirection.direction;
    }


    public void setSeed(String seed) {

        RANDOM.setSeed(Long.parseLong(seed));
    }

    public TETile[][] worldGenerator() {

        /* randomly generate the number of room in our world
         Since we do not want our world with length 30 
         with 29 rooms (too packed !!)
         so I take the liberty to cap it at min(w, h)/2*/

        initialize(tilesets);

        int numRoom = RandomUtils.uniform(RANDOM, Math.min(WIDTH, HEIGHT), Math.max(WIDTH, HEIGHT));

        // start position
        int startX = RandomUtils.uniform(RANDOM, 1, WIDTH - 1);
        int startY = RandomUtils.uniform(RANDOM, 1, HEIGHT - 1);
        int[] position = new int[]{startX, startY};
        Direction direction = new Direction(RANDOM);
        /*
            while num_rom > 0:
                we at a position in time decide we built a room or a hallway
                    if room:
                        numRoom -= 1

         */
        while (numRoom > 0) {

            if (buildDecision() == 1) {
                position = builtRoom(tilesets, position[0], position[1], direction);
                numRoom -= 1;
            } else {
                position = buildHallway(tilesets, position[0], position[1], direction);
            }

        }

        int[] newPos = spawnWallDoorPlayer(tilesets);
        player.setX(newPos[0]);
        player.setY(newPos[1]);
        tilesets[player.getX()][player.getY()] = (new Tileset()).getPlayer();
        createFunStuff();
        return tilesets;
    }

    public int buildDecision() {

        return RandomUtils.uniform(RANDOM, 2);
    }

    public void initialize(TETile[][] world) {

        // initialize tiles
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

    }

    private int lengthDetermination(int xCome, int yCome, WalkDir buildWay) {

        int length = 0;

        while (length == 0) {
            switch (buildWay) {

                case X_RIGHT:
                    length = RandomUtils.uniform(RANDOM, (int) (WIDTH - 1 - xCome));
                    break;
                case X_LEFT:
                    length = RandomUtils.uniform(RANDOM, (int) (xCome - 1));
                    break;
                case Y_UP:
                    length = RandomUtils.uniform(RANDOM, (int) (HEIGHT - 1 - yCome));
                    break;
                case Y_DOWN:
                    length = RandomUtils.uniform(RANDOM, (int) (yCome - 1));
                    break;
                default:
                    break;
            }
        }

        return length;
    }

    // Puts the x and y coordinates of the neighbors 
    // of a point into an array of arrays

    /*
        build a hall-way
     */
    private int[] buildHallway(TETile[][] world, int xCome, int yCome, Direction comeWay) {

        WalkDir buildDir = RandWalkDirEng.buildDir(xCome, yCome, comeWay.direction);
        comeWay.direction = buildDir;
        int length = lengthDetermination(xCome, yCome, buildDir);
        return buildHall(world, length, xCome, yCome, buildDir);
    }


    // Starting at (1, 1) and going to (WIDTH - 1, HEIGHT - 1), 
    // iterates through the points in the grid.
    // if a neighbor of a walkable tile is a non-walkable tile, 
    // create a wall at the neighbor's point and continue iteration

    // Puts the x and y coordinates of the neighbors of a point into an array of arrays

    /*
        Build a Hallway with length at xCome, yCome toward buildDir
     */
    private int[] buildHall(TETile[][] world, int length, int xCome, int yCome, WalkDir buildDir) {

        int xPresent = xCome;
        int yPresent = yCome;

        try {


            switch (buildDir) {

                case X_RIGHT:
                    for (int i = 0; i < length; i++) {
                        world[xCome + i][yCome] = Tileset.FLOOR;
                        //  world[xCome + i + 1][yCome + 1] = Tileset.WALL;
                        //  world[xCome + i + 1][yCome - 1] = Tileset.WALL;
                    }
                    xPresent = xCome + length;
                    break;

                case X_LEFT:
                    for (int i = 0; i < length; i++) {
                        world[xCome - i][yCome] = Tileset.FLOOR;
                        //  world[xCome - i - 1][yCome + 1] = Tileset.WALL;
                        //  world[xCome - i - 1][yCome - 1] = Tileset.WALL;
                    }
                    xPresent = xCome - length;
                    break;

                case Y_UP:
                    for (int i = 0; i < length; i++) {
                        world[xCome][yCome + i] = Tileset.FLOOR;
                        //  world[xCome + 1][yCome + i + 1] = Tileset.WALL;
                        //  world[xCome - 1][yCome + i + 1] = Tileset.WALL;
                    }
                    yPresent = yCome + length;

                    break;

                case Y_DOWN:
                    for (int i = 0; i < length; i++) {
                        world[xCome][yCome - i] = Tileset.FLOOR;
                        //  world[xCome + 1][yCome - i - 1] = Tileset.WALL;
                        //  world[xCome - 1][yCome - i - 1] = Tileset.WALL;
                    }
                    yPresent = yCome - length;
                    break;

                default:
                    break;

            }
        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println(xCome + ", " + yCome + "length " + length);
        }

        return new int[]{xPresent, yPresent};
    }


        /* public static void wallBuilder(TETile[][] world, int xP, int yP) {
            if (world[xP][yP] == Tileset.NOTHING
    }
        */

    private int[] determineRoomDimension(int xCome, int yCome, WalkDir come) {

        //  WalkDir buildDir = RandWalkDirEng.buildDir(come);
        int maxX = Math.min(xCome - 1, (WIDTH - 1 - xCome) / 2);
        int maxY = Math.min(yCome - 1, (HEIGHT - 1 - yCome) / 2);
        int width = RandomUtils.uniform(RANDOM, 1, Math.max(2, maxX));
        int height = RandomUtils.uniform(RANDOM, 1, Math.max(2, maxY));

        return new int[]{width, height};
    }

    private int[] builtRoom(TETile[][] world, int xCome, int yCome, Direction come) {


        int[] arr = determineRoomDimension(xCome, yCome, come.direction);

            /*
                   w, h are dimension of our room
             */
        int w = arr[0];
        int h = arr[1];


        int[] positionAfter = null;


        // built the room depends on which direction you come
        switch (come.direction) {

            case X_RIGHT:

                for (int y = 0; y < h; y++) {
                    buildHall(world, w, xCome, yCome - y, WalkDir.X_RIGHT);
                }
                break;

            case X_LEFT:

                for (int y = 0; y < h; y++) {
                    buildHall(world, w, xCome, yCome + y, WalkDir.X_LEFT);
                }
                break;

            case Y_UP:
                for (int x = 0; x < w; x++) {
                    buildHall(world, h, xCome - x, yCome, WalkDir.Y_UP);
                }
                break;

            case Y_DOWN:
                for (int x = 0; x < w; x++) {
                    buildHall(world, h, xCome + x, yCome, WalkDir.Y_DOWN);
                }
                break;
            default:
                break;

        }

        return roomDirAfterBuild(xCome, w, yCome, h, come);
    }

    private int[] roomDirAfterBuild(int xCome, int width, int yCome, int height, Direction come) {


        WalkDir buildDir = RandWalkDirEng.buildDir(xCome, yCome, come.direction);
        //come.direction = buildDir;

        int xAfter = 0;
        int yAfter = 0;

        if (come.direction == WalkDir.X_RIGHT || come.direction == WalkDir.Y_DOWN) {
            xAfter = xCome + width / 2;
            yAfter = yCome - height / 2;
        } else {
            xAfter = xCome - width / 2;
            yAfter = yCome + height / 2;
        }

        if (width != 1) {
            xAfter += RandomUtils.uniform(RANDOM, -width / 2, width / 2);
        }
        if (height != 1) {
            yAfter += RandomUtils.uniform(RANDOM, -height / 2, height / 2);
        }

        come.direction = buildDir;
        return new int[]{xAfter, yAfter};
    }

    public TETile[][] visionSquare(int xPos, int yPos) {
        TETile[][] neighbors = new TETile[WIDTH][HEIGHT];
        initialize(neighbors);
        int vR = player.getVision();
        for (int i = Math.max(0, xPos - vR); i < Math.min(WIDTH, xPos + vR + 1); i++) {
            for (int k = Math.max(0, yPos - vR); k < Math.min(HEIGHT, yPos + vR + 1); k++) {
                neighbors[i][k] = tilesets[i][k];
            }
        }
        return neighbors;
    }

    private boolean validatedPoint(int x, int y) {

        return (x >= 0 && x < WIDTH) && (y >= 0 && y < HEIGHT);
    }

    public boolean validWaterNeighbor(TETile tile) {
        return !tile.description().equals("null")
                && !tile.description().equals("wall");
    }

    public boolean notHallway(int xPos, int yPos) {
        int[][] tempN = neighbors(xPos, yPos);
        int wallCount = 0;
        for (int i = 0; i < tempN.length; i++) {
            if (tilesets[tempN[0][i]][tempN[1][i]].equals(Tileset.WALL)) {
                wallCount++;
            }
        }
        return wallCount < 4;
    }

    private void createWater() {
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                int randomWater = RandomUtils.uniform(RANDOM, -1, 70);
                if (randomWater < 0 && tilesets[i][k].equals(Tileset.FLOOR)) {
                    tilesets[i][k] = Tileset.WATER;
                    int[][] tempNeighbors = neighbors(i, k);
                    for (int j = 0; j < tempNeighbors[0].length; j++) {
                        int xPos = tempNeighbors[0][j];
                        int yPos = tempNeighbors[1][j];
                        TETile validTile = tilesets[xPos][yPos];
                        if (validatedPoint(xPos, yPos)
                                && validWaterNeighbor(validTile)
                                && notHallway(xPos, yPos)) {
                            int randomWaterNeighbor = RandomUtils.uniform(RANDOM, -1, 3);
                            if (randomWaterNeighbor > 0) {
                                tilesets[xPos][yPos] = Tileset.WATER;
                            }
                        }
                    }

                }
            }
        }
    }

    public TETile randomFlowerGenerator() {
        int randomFlower = RandomUtils.uniform(RANDOM, 0, 6);
        switch (randomFlower) {
            default:
                break;
            case 0:
                return Tileset.REDFLOWER;
            case 1:
                return Tileset.ORANGEFLOWER;
            case 2:
                return Tileset.YELLOWFLOWER;
            case 3:
                return Tileset.MAGENTAFLOWER;
            case 4:
                return Tileset.PINKFLOWER;
            case 5:
                return Tileset.WHITEFLOWER;
        }
        return null;
    }

    private void createFunStuff() {
        createWater();
        createFlowers();
        createSwimAndDigAndNormal();
        createTorchTile();
    }

    private void createFlowers() {
        int maxFlowers = 20;
        while (maxFlowers > 0) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < HEIGHT; k++) {
                    int randomFlower = RandomUtils.uniform(RANDOM, -1, 500);
                    if (tilesets[i][k].equals(Tileset.FLOOR) && randomFlower < 0) {
                        tilesets[i][k] = randomFlowerGenerator();
                        maxFlowers--;
                        if (maxFlowers == 0) {
                            break;
                        }
                    }
                }
                if (maxFlowers == 0) {
                    break;
                }
            }
        }
    }

    private int countFlowers() {
        int flowerCount = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int k = 0; k < HEIGHT; k++) {
                if (tilesets[i][k].description() == "flowers") {
                    flowerCount++;
                }
            }
        }
        return flowerCount;
    }

    private void createSwimAndDigAndNormal() {
        boolean digTrigger = false;
        boolean swimTrigger = false;
        boolean normalTrigger = false;
        while (!digTrigger) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < HEIGHT; k++) {
                    int randomDig = RandomUtils.uniform(RANDOM, -1, 1200);
                    if (tilesets[i][k].equals(Tileset.FLOOR) && randomDig < 0) {
                        tilesets[i][k] = Tileset.DIG;
                        digTrigger = true;
                        break;
                    }
                }
                if (digTrigger) {
                    break;
                }
            }
        }
        while (!swimTrigger) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < HEIGHT; k++) {
                    int randomSwim = RandomUtils.uniform(RANDOM, -1, 1200);
                    if (tilesets[i][k].equals(Tileset.FLOOR) && randomSwim < 0
                            && !tilesets[i][k].equals(Tileset.DIG)) {
                        tilesets[i][k] = Tileset.SWIM;
                        swimTrigger = true;
                        break;
                    }
                }
                if (swimTrigger) {
                    break;
                }
            }
        }
        while (!normalTrigger) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < HEIGHT; k++) {
                    int randomNormal = RandomUtils.uniform(RANDOM, -1, 1200);
                    if (tilesets[i][k].equals(Tileset.FLOOR) && randomNormal < 0
                            && !tilesets[i][k].equals(Tileset.DIG)
                            && !tilesets[i][k].equals(Tileset.SWIM)) {
                        tilesets[i][k] = Tileset.NORMAL;
                        normalTrigger = true;
                        break;
                    }
                }
                if (normalTrigger) {
                    break;
                }
            }
        }
    }

    private void createTorchTile() {
        boolean torchTrigger = false;
        while (!torchTrigger) {
            for (int i = 0; i < WIDTH; i++) {
                for (int k = 0; k < HEIGHT; k++) {
                    int randomTorch = RandomUtils.uniform(RANDOM, -1, 1000);
                    if (tilesets[i][k].equals(Tileset.FLOOR) && randomTorch < 0) {
                        tilesets[i][k] = Tileset.TORCH;
                        torchTrigger = true;
                        break;
                    }
                }
                if (torchTrigger) {
                    break;
                }
            }
        }
    }


    public void moveFunction(char character) {

        // character = java.lang.Character.toLowerCase(character);

        switch (character) {
            case 'w':
                legalMove(WalkDir.Y_UP);
                break;
            case 's':
                legalMove(WalkDir.Y_DOWN);
                break;
            case 'a':
                legalMove(WalkDir.X_LEFT);
                break;
            case 'd':
                legalMove(WalkDir.X_RIGHT);
                break;
            default:
                break;
        }
    }

    public void updateFunction(char C) {
        C = java.lang.Character.toLowerCase(C);
        if (C == 'w' || C == 's' || C == 'a' || C == 'd') {
            moveFunction(C);
        } else if (C == ':') {
            File f = new File("./world.txt");
            try {
                if (!f.exists()) {
                    f.createNewFile();
                }
                FileOutputStream fs = new FileOutputStream(f);
                ObjectOutputStream os = new ObjectOutputStream(fs);
                os.writeObject(this);
                os.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }

    public void setWorld(TETile[][] world) {
        tilesets = world;
    }

    public TETile[][] getTilesets() {
        return tilesets;
    }


    public TETile[][] setUpGame(String inputString) {

        String seed = "";
        int i = 0;
        if (inputString.charAt(0) == 'N' || inputString.charAt(0) == 'n') {
            int count = 1;

            while (inputString.charAt(count) != 's' && inputString.charAt(count) != 'S') {
                count++;
            }

            seed = inputString.substring(1, count);
            setSeed(seed);
            worldGenerator();
            i = count + 1;

        } else if (inputString.charAt(0) == 'L' || inputString.charAt(0) == 'l') {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(new File("world.txt")));
                World w = (World) (in.readObject());
                setWorld(w);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i = 1;
        }
        while (i < inputString.length()) {
            updateFunction(inputString.charAt(i));
            i++;
        }
        return getTilesets();
    }

    public int randomVisionInt() {
        return RandomUtils.uniform(RANDOM, -1, 30);
    }


    private void legalMove(WalkDir direction) {
        switch (direction) {
            case X_LEFT:
                if (player.getX() > 0 && walkableTile(tilesets[player.getX() - 1][player.getY()])) {
                    tilesets[player.getX()][player.getY()] = player.getUnder();
                    player.setX(player.getX() - 1);
                    player.setUnder(tilesets[player.getX()][player.getY()]);
                    tilesets[player.getX()][player.getY()] = player.getModel();
                }
                break;
            case X_RIGHT:
                if (player.getX() < WIDTH - 1
                    && walkableTile(tilesets[player.getX() + 1][player.getY()])) {
                    tilesets[player.getX()][player.getY()] = player.getUnder();
                    player.setX(player.getX() + 1);
                    player.setUnder(tilesets[player.getX()][player.getY()]);
                    tilesets[player.getX()][player.getY()] = player.getModel();
                }
                break;
            case Y_UP:
                if (player.getY() < HEIGHT - 1
                        && walkableTile(tilesets[player.getX()][player.getY() + 1])) {
                    tilesets[player.getX()][player.getY()] = player.getUnder();
                    player.setY(player.getY() + 1);
                    player.setUnder(tilesets[player.getX()][player.getY()]);
                    tilesets[player.getX()][player.getY()] = player.getModel();
                }
                break;
            case Y_DOWN:
                if (player.getY() > 0 && walkableTile(tilesets[player.getX()][player.getY() - 1])) {
                    tilesets[player.getX()][player.getY()] = player.getUnder();
                    player.setY(player.getY() - 1);
                    player.setUnder(tilesets[player.getX()][player.getY()]);
                    tilesets[player.getX()][player.getY()] = player.getModel();
                }
                break;
            default:
                break;
        }
        updatePlayer();
    }

    public void updatePlayer() {
        if (randomVisionInt() < 0) {
            player.setVision(player.getVision() - 1);
        }
        if (player.getModel().description().equals("god")
            && player.getUnder().description().equals("locked door")) {
            new Menu().winScreen();
        }
        if (player.getUnder().description().equals("swim")
            && !player.getModel().description().equals("god")) {
            player.setModel(Tileset.SWIMMER);
        }
        if (player.getUnder().description().equals("dig")
            && !player.getModel().description().equals("god")) {
            player.setModel(Tileset.DIGGER);
        }
        if (player.getUnder().description().equals("normal")
            && !player.getModel().description().equals("god")) {
            player.setModel(Tileset.getPlayer());
        }
        if (player.getUnder().description().equals("torch")) {
            player.setVision(player.torchTile());
            player.setUnder(Tileset.FLOOR);
        }
        if (player.getUnder().description().equals("flower")) {
            player.setFlowerCount(player.getFlowerCount() + 1);
            player.setVision(player.getVision() + 1);
            player.setUnder(Tileset.FLOOR);
            if (player.getFlowerCount() == 6) {
                player.setModel(Tileset.GOD);
                player.setVision(Math.min(WIDTH, HEIGHT));
            }
        }
    }

    public boolean playGame() {
        while (player.getVision() != 0) {
            if (player.getVision() == 0) {
                new Menu().loseScreen();
            }
            char input = '1';
            if (StdDraw.hasNextKeyTyped()) {
                input = java.lang.Character.toLowerCase(StdDraw.nextKeyTyped());
            }
            switch (input) {
                case 'w':
                    legalMove(WalkDir.Y_UP);
                    break;
                case 's':
                    legalMove(WalkDir.Y_DOWN);
                    break;
                case 'a':
                    legalMove(WalkDir.X_LEFT);
                    break;
                case 'd':
                    legalMove(WalkDir.X_RIGHT);
                    break;
                case 'q':
                    return true;
                default:
                    break;
            }
            render();
            //   mouseDisplay();
        }
        new Menu().loseScreen();
        return false;
    }


    public boolean startGame() {
        this.worldGenerator();
        return playGame();
    }


    /*
        Mouse hover detected, should be used after render();
     */
    public void mouseDisplay(TETile[][] world) {

        int xPos = (int) StdDraw.mouseX();
        int yPos = (int) StdDraw.mouseY();

        if (xPos >= 0 && yPos < WIDTH && yPos >= 0 && yPos < HEIGHT) {

            try {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                StdDraw.text(WIDTH * 1 / 10, HEIGHT * 99 / 100, world[xPos][yPos].description());
                StdDraw.show();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(xPos + " " + yPos);
            }


        }
    }
}




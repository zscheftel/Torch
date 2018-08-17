package byog.Core;

import java.io.Serializable;
import java.util.Random;

public class RandWalkDirEng implements Serializable {
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private static WalkDir[] arr = {WalkDir.X_RIGHT, WalkDir.X_LEFT, WalkDir.Y_UP, WalkDir.Y_DOWN};

    public static WalkDir buildDir(int xCome, int yCome, WalkDir comeWay) {


        if (comeWay == WalkDir.X_LEFT || comeWay == WalkDir.X_RIGHT) {

            if (yCome > HEIGHT - yCome) {
                return WalkDir.Y_DOWN;
            } else {
                return WalkDir.Y_UP;
            }

        } else {

            if (xCome > WIDTH - xCome) {
                return WalkDir.X_LEFT;
            } else {
                return WalkDir.X_RIGHT;
            }

        }


    }
    /* public static WalkDir RoomDirection(int xCome, int yCome, WalkDir comeWay) {
        if (comeWay == WalkDir.Y_DOWN || comeWay == WalkDir.X_RIGHT) {

        }
        else {

        }
    }
    */


    public static WalkDir randomDirection(Random r) {

        return arr[RandomUtils.uniform(r, 4)];
    }
}

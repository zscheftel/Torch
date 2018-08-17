package byog.Core;

import java.io.Serializable;
import java.util.Random;


public class Direction implements Serializable {

    WalkDir direction;

    Direction(Random r) {

        direction = RandWalkDirEng.randomDirection(r);
    }
}

package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

import static byog.Core.GenerateMap.MAP_HEIGHT;
import static byog.Core.GenerateMap.MAP_WIDTH;

//和位置有关，以及移动的一些操作
public class Position implements Serializable {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position newPosition(TETile[][] world, char m) {
        Position res = new Position(x, y);
        switch (m) {
            case 'w':
            case 'W':
                res.y = y + 1 >= MAP_HEIGHT ? y : y + 1;
                break;
            case 'a':
            case 'A':
                res.x = x - 1 < 0 ? x : x - 1;
                break;
            case 's':
            case 'S':
                res.y = y - 1 < 0 ? y : y - 1;
                break;
            case 'd':
            case 'D':
                res.x = x + 1 >= MAP_WIDTH ? x : x + 1;
                break;
            default:
                break;
        }
        return world[res.x][res.y].equals(Tileset.WALL) ? this : res;
    }

    public void move(TETile[][] world, Position np) {
        if (!this.equals(np)) {
            world[x][y] = Tileset.FLOOR;
            world[np.x][np.y] = Tileset.FLOWER;
        }
        x = np.x;
        y = np.y;
    }
}

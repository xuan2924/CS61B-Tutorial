package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

import static byog.Core.Game.HEIGHT;
import static byog.Core.Game.WIDTH;
import static byog.Core.Room.minRoomHeight;
import static byog.Core.Room.minRoomWidth;

public class GenerateMap {
    private static long seed;
    public static Random RANDOM;
    private static int roomNum; //房间数

    private static final int changeSize = 16;
    public static int MAP_WIDTH = WIDTH / changeSize;
    public static int MAP_HEIGHT = HEIGHT / changeSize;

    private static Position[] positions;
    private static TETile[][] world = new TETile[MAP_WIDTH][MAP_HEIGHT];

    public static void createRandom(int num, int width, int height, TETile t) {
        positions = new Position[num];
        for (int i = 0; i < num; i += 1) {
            Position p = new Position(RANDOM.nextInt(width), RANDOM.nextInt(height));
            Room.createRoom(world, RANDOM.nextInt(10) + minRoomWidth, RANDOM.nextInt(10) + minRoomHeight, p, t);
            positions[roomNum] = p;
            roomNum += 1;
        }
    }

    //x方向走廊
    public static void conectX(Position x, Position y) {
        int start = Math.min(x.x, y.x);
        int end = Math.max(x.x, y.x);
        int xMax = world[0].length;
        int yMax = world.length;
        end = end >= yMax ? yMax - 1 : end;
        start = start >= yMax ? yMax - 1 : start;
        for (int i = start; i < end; i += 1) {
            for (int j = 0; j < 2; j += 1) {
                if (x.y + j >= xMax) {
                    continue;
                }
                world[i][x.y + j] = Tileset.FLOOR;
            }
        }
    }

    // y方向走廊
    public static void conectY(Position x, Position y) {
        int start = Math.min(x.y, y.y);
        int end = Math.max(x.y, y.y);
        int xMax = world[0].length;
        int yMax = world.length;
        end = end >= xMax ? xMax - 1 : end;
        start = start >= xMax ? xMax - 1 : start;
        for (int i = start; i < end; i += 1) {

            for (int j = 0; j < 2; j += 1) {
                if (y.x + j >= yMax) {
                    continue;
                }
                world[y.x + j][i] = Tileset.FLOOR;
            }
        }
    }

    //连接孤立的矩形块
    public static void connectFloor() {
        for (int i = 0; i < roomNum - 1; i += 1) {
            conectX(positions[i], positions[i + 1]);
            conectY(positions[i], positions[i + 1]);
        }
    }

    //用墙围起来
    public static void setWall(int width, int height) {
        Position p = new Position(0, 0);
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                p.x = i;
                p.y = j;
                if (check(p)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    //检查周围是不是有地砖
    public static boolean check(Position p) {
        int width = world.length;
        int height = world[0].length;
        int count = 0;
        if (world[p.x][p.y].equals(Tileset.FLOOR)) {
            return false;
        }
        for (int i = -1; i <= 1; i += 1) {
            if (p.x + i < 0 || p.x + i >= width) {
                continue;
            }
            for (int j = -1; j <= 1; j += 1) {
                if (p.y + j < 0 || p.y + j >= height) {
                    continue;
                }
                if (i == 0 && j == 0) {
                    continue;
                }
                if (world[p.x + i][p.y + j].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }

    //初始化矩形部分
    public static void setInitialize(int width, int height) {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    //主函数测试用
    public static TETile[][] generate(Position userp, long s) {
        seed = s;
        RANDOM = new Random(s);
        roomNum = 0;
        int numberOfRooms = 12;
        setInitialize(MAP_WIDTH, MAP_HEIGHT);
        //createFloor(world, 5, 5, new Position(width / 2, height / 2), Tileset.FLOOR);
        createRandom(numberOfRooms, MAP_WIDTH, MAP_HEIGHT, Tileset.FLOOR);
        connectFloor();
        setWall(MAP_WIDTH, MAP_HEIGHT);
        if (!world[userp.x][userp.y].equals(Tileset.FLOOR)) {
            for (int i = RANDOM.nextInt(MAP_WIDTH / 2); i < MAP_WIDTH; i += 1) {
                boolean find = false;
                for (int j = RANDOM.nextInt(MAP_HEIGHT / 2); j < MAP_HEIGHT; j += 1) {
                    if (world[i][j].equals(Tileset.FLOOR)) {
                        world[i][j] = Tileset.FLOWER;
                        find = true;
                        userp.x = i;
                        userp.y = j;
                        break;
                    }
                }
                if (find) {
                    break;
                }
            }
        } else {
            world[userp.x][userp.y] = Tileset.FLOWER;
        }
        return world;
    }

}

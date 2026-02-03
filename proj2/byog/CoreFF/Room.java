package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

import static byog.Core.Game.HEIGHT;
import static byog.Core.Game.WIDTH;

public class Room {
    //种子
    private static long seed;
    private static Random RANDOM = new Random(12345l);
    private static Position[] positions;
    private static int size = 0;
    public static  int widthR = WIDTH/16;
    public static int heightR = HEIGHT/16;
    public static TETile[][] world = new TETile[widthR][heightR];

    public static void setRoomSeed(long s) {
        seed = s;
        RANDOM.setSeed(s);

    }


    //位置类
    public static class Position implements Serializable {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position() {
            x = 0;
            y = 0;
        }
    }


    //1.生成一块矩形的地板
    public static void createFloor(TETile[][] world, int width, int height, Position p, TETile t) {
        int xMax = world.length;
        int yMax = world[0].length;
        for (int i = -width / 2; i < width / 2; i += 1) {
            for (int j = -height / 2; j < height / 2; j += 1) {
                int xCord = p.x + i;
                int yCord = p.y + j;
                if (xCord >= xMax || xCord <= 0 || yCord >= yMax || yCord < 0) {
                    continue;
                }
                world[xCord][yCord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
        }
    }

    //2.随机生成各个矩形地板
    public static void createRandom(TETile[][] world, int num, int width, int height, TETile t) {
        positions = new Position[num];
        for (int i = 0; i < num; i += 1) {
            Position p = new Position(RANDOM.nextInt(width) + 3, RANDOM.nextInt(height) + 3);
            createFloor(world, RANDOM.nextInt(10) + 3, RANDOM.nextInt(10) + 3, p, t);
            positions[size] = p;
            size += 1;
        }
    }

    //3.1 x方向走廊
    public static void conectX(TETile[][] world, Position x, Position y) {
        int start = x.x > y.x ? y.x : x.x;
        int end = x.x <= y.x ? y.x : x.x;
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

    //3.2 y方向走廊
    public static void conectY(TETile[][] world, Position x, Position y) {
        int start = x.y <= y.y ? x.y : y.y;
        int end = x.y > y.y ? x.y : y.y;
        int xMax = world[0].length;
        int yMax = world.length;
        end = end >= xMax ? xMax : end;
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

    //3.3连接孤立的矩形块
    public static void connectFloor(TETile[][] world) {
        for (int i = 0; i < size - 1; i += 1) {
            conectX(world, positions[i], positions[i + 1]);
            conectY(world, positions[i], positions[i + 1]);
        }
    }

    //4.用墙围起来
    public static void setWall(TETile[][] world, int width, int height) {
        Position p = new Position();
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                p.x = i;
                p.y = j;
                if (check(world, p)) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    //4.1检查周围是不是有地砖
    public static boolean check(TETile[][] world, Position p) {
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

    //5.初始化矩形部分
    public static void setInitialize(TETile[][] world, int width, int height) {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    //主函数测试用
    public static void generate(Position userp) {
        TERenderer ter = new TERenderer();
        ter.initialize(widthR, heightR);
        setInitialize(world, widthR, heightR);
        //createFloor(world, 5, 5, new Position(width / 2, height / 2), Tileset.FLOOR);
        createRandom(world, 10, widthR, heightR, Tileset.FLOOR);
        connectFloor(world);
        setWall(world, widthR, heightR);
        if (!world[userp.x][userp.y].equals(Tileset.FLOOR)) {
            for (int i = RANDOM.nextInt(widthR / 2); i < widthR; i += 1) {
                boolean find = false;
                for (int j = RANDOM.nextInt(heightR / 2); j < heightR; j += 1) {
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
        ter.renderFrame(world);
    }
}
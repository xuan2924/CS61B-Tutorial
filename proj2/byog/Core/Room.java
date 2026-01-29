package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Room {
    //种子
    private static final Random RANDOM = new Random(1234L);

    //位置类
    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //1.生成一块矩形的地板
    public static void createFloor(TETile[][] world, int height, int width, Position p, TETile t) {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                int xCord = p.x + i;
                int yCord = p.y + j;
//                int xMax = world.length;
//                int yMax = world[0].length;
//                if(xCord > xMax || xCord < 0 || yCord > yMax || yCord < 0){
//                    continue;
//                }
                world[xCord][yCord] = TETile.colorVariant(t,32,32,32,RANDOM);
            }
        }
    }

    //2.随机生成各个矩形地板
    public static void createRandom(TETile[][] world, int num) {

    }

    //3.连接孤立的矩形块
    public static void connectFloor(TETile[][] world) {

    }

    //4.用墙围起来
    public static void setWall(TETile[][] world, int num) {

    }

    //5.初始化矩形部分
    public static void setInitialize(TETile[][] world, int height, int width){
        for(int i = 0; i < width; i += 1){
            for(int j = 0; j < height; j += 1){
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    //主函数测试用
    public static void main(String[] args) {
        int height = 80;
        int width = 40;
        TERenderer ter = new TERenderer();
        ter.initialize(width,height);

        TETile[][] world = new TETile[width][height];

        setInitialize(world,height,width);
        createFloor(world, 5,5,new Position(width / 2,height / 2),Tileset.FLOOR);

        ter.renderFrame(world);
    }
}
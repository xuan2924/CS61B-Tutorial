package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.util.Random;

public class Room {
    //种子
    private static final Random RANDOM = new Random(1234L);

    //位置类
    public static class Position{
        int x;
        int y;

        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    //1.生成一块矩形的地板
    public static void createFloor(TETile[][] world, int width, int height, Position p) {

    }

    //2.随机生成各个矩形地板
    public static void createRandom(TETile[][] world, int num){

    }

    //3.连接孤立的矩形块
    public static void connectFloor(TETile[][] world){

    }

    //4.用墙围起来
    public static void setWall(TETile[][] world, int num){

    }

    //主函数测试用
    public static void main(String[] args){
        int width = 80;
        int height = 50;
        TERenderer ter = new TERenderer();

        TETile[][] world = new TETile[height][width];

    }
}
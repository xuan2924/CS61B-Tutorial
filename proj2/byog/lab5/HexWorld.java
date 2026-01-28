package byog.lab5;
import org.junit.Test;

import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import javax.swing.text.Position;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final Random RANDOM = new Random(12345);

    public static class Position{
        int x;
        int y;

        public Position(int x,int y){
            this.x = x;
            this.y = y;
        }

    }


    /**
     * 计算大小为 s 的六边形第 i 行的宽度
     * @param s 六边形变长
     * @param i 行号，其中i = 0是最底层
     * @return 该行的瓷砖数量
     */
    public static int hexRowWidth(int s, int i){
        if(i < s){
            return s + 2 * i;
        }else {
            return s + 2 * (s - 1) + 2 * (s - i);
        }
    }

    @Test
    public void testHexRowWidth(){
        assertEquals(2,hexRowWidth(2,0));
        assertEquals(3,hexRowWidth(3,0));
        assertEquals(4,hexRowWidth(2,1));
        assertEquals(4,hexRowWidth(2,2));
        assertEquals(5,hexRowWidth(3,4));
        assertEquals(5,hexRowWidth(3,1));;
    }

    /**
     * 计算六边形第 i 行最左侧瓷砖的相对 x 坐标
     * 最底层的 x 坐标为 0
     */
    public static int hexRowOffset(int s, int i){
        if(i < s){
            return  (-i);
        }else {
            return -(2 * s - 1- i);
        }
    }

    @Test
    public void testHexRowOffset(){
        assertEquals(0,hexRowOffset(2,0));
        assertEquals(0,hexRowOffset(2,3));
        assertEquals(-2,hexRowOffset(3,2));
        assertEquals(-2,hexRowOffset(3,3));
        assertEquals(-4,hexRowOffset(5,5));
    }

    /**
     * 添加一行相同的瓷砖
     * @param world 绘制的目标世界
     * @param p 该行最左侧的位置坐标
     * @param width 绘制的宽度（瓷砖数量）
     * @param t 瓷砖类型
     */
    private static void addRow(TETile[][] world, Position p, int width, TETile t){
        for(int i = 0; i < width; i += 1){
            int xCord = p.x + i;
            int yCord = p.y ;
            world[xCord][yCord] = TETile.colorVariant(t,32,32,32,RANDOM);
        }
    }

    /**
     * 向世界中添加一个六边形
     * @param world 绘制的目标世界
     * @param p 六边形左下角的坐标
     * @param s 六边形的大小
     * @param t 瓷砖类型
     */
    public static void addHexagon(TETile[][] world, Position p,int s, TETile t){
        if(s < 2){
            throw new IllegalArgumentException("Hexagon must be at least size 2");
        }

        for(int yi = 0; yi < 2*s; yi += 1){
            int thisRow = p.y + yi;

            int xRowStart = p.x + hexRowOffset(s,yi);
            Position rowStartP = new Position(xRowStart, thisRow);

            int rowWidth = hexRowWidth(s,yi);

            addRow(world,rowStartP,rowWidth,t);
        }

    }

    public static void main(String[] args){
        //1.初始化渲染器
        int width = 50;
        int height = 60;
        TERenderer ter = new TERenderer();
        ter.initialize(width,height);

        //2.初始化世界数组
        TETile[][] world = new TETile[width][height];
        for(int i = 0; i < width; i += 1){
            for(int j = 0; j< height; j+= 1){
                world[i][j] = Tileset.NOTHING;
            }
        }

        //3.调用方法画一个六边形
        Position pos = new Position(20,20);
        addHexagon(world,pos,3,Tileset.FLOWER);

        //4.画多个不同大小进行测试
        addHexagon(world,new Position(10,10),6,Tileset.GRASS);
        addHexagon(world, new Position(35, 35), 4, Tileset.GRASS);

        //渲染到屏幕上
        ter.renderFrame(world);
    }
}

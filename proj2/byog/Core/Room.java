package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.HexWorld;
import org.junit.Test;

import javax.swing.plaf.PanelUI;
import java.util.Random;

public class Room {
    private static final Random RANDOM = new Random(12345);

    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 墙的外框，填补一个框
     *
     * @param world
     * @param p
     * @param row
     * @param col
     * @param t
     */
    public static void addSet(TETile[][] world, Position p, int row, int col, TETile t) {
        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < col; j += 1) {
                int xCord = p.x + i;
                int yCord = p.y + j;
                if (world[xCord][yCord].equals(Tileset.WALL)) {
                    continue;
                }
                if (i == 0 || i == row - 1) {
                    world[xCord][yCord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
                } else if (j == 0 || j == col - 1) {
                    world[xCord][yCord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
                } else {
                    continue;
                }
            }
        }
    }

    public static void genetateMap(TETile[][] world, int number,int width, int height, TETile t) {
        Random RandomX = new Random(width/2);
        Random RandonY = new Random(height/2);
        for (int i = 0; i < number; i++){
            int x = RandomX.nextInt(width/2);
            int y = RandonY.nextInt(height/2);
            addSet(world,new Position(x,y),x,y,t);
        }
    }

    public static void main(String[] args) {
        int width = 40;
        int height = 40;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        genetateMap(world,100,width,height,Tileset.WALL);

        ter.renderFrame(world);
    }

}
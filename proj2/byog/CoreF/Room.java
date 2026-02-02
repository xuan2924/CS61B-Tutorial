package byog.CoreF;

import byog.TileEngine.TETile;

import static byog.CoreF.GenerateMap.RANDOM;


public class Room {
    public static int minRoomWidth = 3;
    public static int minRoomHeight = 3;

    public static void createRoom(TETile[][] world, int width, int height, Position p, TETile t) {
        int xMax = world.length;
        int yMax = world[0].length;
        for (int i = -width / 2; i < width / 2; i += 1) {
            for (int j = -height / 2; j < height / 2; j += 1) {
                int xCord = p.x + i;
                int yCord = p.y + j;
                //两边留一些，避免铺满
                if (xCord >= xMax - 1  || xCord <= 0|| yCord >= yMax - 1 || yCord <= 0) {
                    continue;
                }
                world[xCord][yCord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
        }
    }
}
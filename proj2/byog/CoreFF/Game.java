package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Scanner;

import static byog.Core.Room.*;

//import static byog.Core.Room.world;

public class Game {
    static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        drawMenu();
        playGame();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public void playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        int sum = 0;
        for (int i = 0; i < input.length(); i += 1) {
            if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                sum = sum * 10 + input.charAt(i) - '0';
            }
        }
        Room.setRoomSeed(sum);
        Room.Position start = new Room.Position(3, 6);
        Room.generate(start);
        while (true) {
            if(StdDraw.hasNextKeyTyped()){
                if(StdDraw.isMousePressed()){
                    double x = StdDraw.mouseX();
                    double y = StdDraw.mouseY();
                    StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                    if(Room.world[(int)x][(int)y].equals(Tileset.WALL)){
                        StdDraw.text(0,0,"WALL");
                    }
                    else if(Room.world[(int)x][(int)y].equals(Tileset.FLOOR)){
                        StdDraw.text(0,0,"FLOOR");
                    }
                    else if(Room.world[(int)x][(int)y].equals(Tileset.NOTHING))
                    {
                        StdDraw.text(0,0,"NOTHING");
                    }

                }
                char ch =StdDraw.nextKeyTyped();
                if(ch == 'q'){
                    GameSave.UserLoad u = new GameSave.UserLoad(start, (int) sum);
                    GameSave.saveWorld(u);

                    System.exit(0);
                }
                moveE(Room.world,start,move(Room.world,start,ch));
                start = move(Room.world,start,ch);
                StdDraw.show();
                ter.renderFrame(Room.world);
            }
        }
    }

    public static void drawMenu() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear();
        StdDraw.clear(Color.black);

        //双缓冲才可以show()
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 4 / 5, "CS61B: THE GAME");

        Font smallFront = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(smallFront);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "New Game (N)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 30, "Load Game (L)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 60, "Quit (Q)");
        StdDraw.show();
    }

    public static void hnitSeed() {
        StdDraw.clear(Color.black);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "Please input a seed");
        StdDraw.show();
    }

    public static Room.Position move(TETile[][] world, Room.Position p, char m) {
        Room.Position res = new Room.Position(p.x, p.y);
        switch (m) {
            case 'w':
                res.y = p.y + 1 >= heightR ? p.y : p.y + 1;
                break;
            case 'a':
                res.x = p.x - 1 < 0 ? p.x : p.x - 1;
                break;
            case 's':
                res.y = p.y - 1 < 0 ? p.y : p.y - 1;
                break;
            case 'd':
                res.x = p.x + 1 >= widthR ? p.x : p.x + 1;
                break;
        }
        return  world[res.x][res.y].equals(Tileset.WALL) ? p:res;
    }

    public static void moveE(TETile[][] world, Room.Position p , Room.Position np){
        if(!p.equals(np)){
            world[p.x][p.y]=Tileset.FLOOR;
            world[np.x][np.y]=Tileset.FLOWER;
        }
        p = np;
    }


    public static void playGame() {
        while (true) {

            if (StdDraw.hasNextKeyTyped()) {
                char input = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (input == 'l') {

                    System.out.println("Load");
                    GameSave.UserLoad u = GameSave.loadWorld();
                    Room.Position start = u.pos;
                    int seed = u.seed;
                    Room.setRoomSeed(seed);
                    Room.generate(start);

                    while (true) {
                        if(StdDraw.hasNextKeyTyped()){

                            StdDraw.setPenColor(Color.RED);
                                double x = StdDraw.mouseX();
                                double y = StdDraw.mouseY();
                                StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                                if(Room.world[(int)x][(int)y].equals(Tileset.WALL)){
                                    StdDraw.text(0,0,"WALL");
                                }
                                else if(Room.world[(int)x][(int)y].equals(Tileset.FLOOR)){
                                    StdDraw.text(0,0,"FLOOR");
                                }
                                else if(Room.world[(int)x][(int)y].equals(Tileset.NOTHING))
                                {
                                    StdDraw.text(0,0,"NOTHING");
                                }


                            char ch =StdDraw.nextKeyTyped();
                            if(ch == 'q'){
                                 u = new GameSave.UserLoad(start, (int) seed);
                                GameSave.saveWorld(u);

                                System.exit(0);
                            }
                            moveE(Room.world,start,move(Room.world,start,ch));
                            start = move(Room.world,start,ch);

                            StdDraw.clear();

                            ter.renderFrame(Room.world);
                            StdDraw.show();
                        }
                    }

                } else if (input == 'q') {
                    System.out.println("Quit");


                } else if (input == 'n') {
                    hnitSeed();
                    long seed = 0;
                    Scanner scanner = new Scanner(System.in);
                    seed = scanner.nextLong();
                    Room.Position start = new Room.Position(3, 6);
                    Room.setRoomSeed(seed);
                    Room.generate(start);
                    while (true) {
                       if(StdDraw.hasNextKeyTyped()){
                           char ch =StdDraw.nextKeyTyped();
                           if(ch == 'q'){
                               GameSave.UserLoad u = new GameSave.UserLoad(start, (int) seed);
                               GameSave.saveWorld(u);

                               return;
                           }
                            moveE(Room.world,start,move(Room.world,start,ch));
                            start = move(Room.world,start,ch);
                            ter.renderFrame(Room.world);
                       }
                    }

                }
            }

            StdDraw.pause(50);
        }

    }

}

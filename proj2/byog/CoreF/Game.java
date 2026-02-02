package byog.CoreF;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byog.CoreF.GenerateMap.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        drawMenu();
        TETile[][] world = new TETile[MAP_WIDTH][MAP_HEIGHT];
        TERenderer ter = new TERenderer();
        playGame(world,new Position(0,0),0,ter);
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
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toLowerCase();
        Position start = new Position(0, 0);
        TETile[][] world = new TETile[MAP_WIDTH][MAP_HEIGHT];
        int seed = 0;
        switch (input.charAt(0)) {
            case 'n':
                int i;
                for (i = 1; i < input.length(); i++) {
                    if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                        seed = seed * 10 + input.charAt(i) - '0';
                    } else {
                        world = generate(start,seed);
                        break;
                    }
                }
                i -- ;
                for(;i < input.length();i++){
                    if(input.charAt(i)=='q')
                    {
                        break;
                    }
                    Position np = start.newPosition(world,input.charAt(i));
                    start.move(world,np);
                }
                break;
            case 'l':
                GameSave.UserLoad u = GameSave.loadWorld();
                start = u.pos;
                seed = u.seed;
                world = generate(start,seed);
                break;
            case 'q':
                System.exit(0);
        }
        return world;
    }


    public void drawMenu() {
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

    public void inputSeed() {
        StdDraw.clear(Color.black);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "Please input a seed");
        StdDraw.show();
    }

    public void playGame(TETile[][] world, Position start, int seed, TERenderer ter) {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (input == 'q') {
                    System.exit(0);
                } else if (input == 'l') {
                    System.out.println("Load");
                    GameSave.UserLoad u = GameSave.loadWorld();
                    start = u.pos;
                    seed = u.seed;
                    world = generate(start,seed);
                    playingGame(world, start, seed, ter);
                } else if (input == 'n') {
                    inputSeed();
                    seed = 0;
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char ch = StdDraw.nextKeyTyped();
                            if (ch >= '0' && ch <= '9') {
                                seed = seed * 10 - ch - '0';
                            }
                            if (ch == 's' || ch == 'S') {
                                break;
                            }
                        }
                    }
                    world = generate(start,seed);
                    playingGame(world, start, seed, ter);
                }
            }
        }
    }

    public void playingGame(TETile[][] world, Position curr, int seed, TERenderer ter) {
        ter.initialize(MAP_WIDTH, MAP_HEIGHT);
        while (true) {
            //StdDraw.clear();
            if (StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                if (ch == 'q') {
                    GameSave.UserLoad u = new GameSave.UserLoad(curr, (int) seed);
                    GameSave.saveWorld(u);
                    System.exit(0);
                }
                Position next = curr.newPosition(world, ch);
                curr.move(world, next);
            }
            ter.renderFrame(world);
            hubShow(world);
            StdDraw.show();
            StdDraw.pause(50);
        }
    }

    public void hubShow(TETile[][] world) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        String hub = "";

        if (x >= 0 && x < MAP_WIDTH && y >= 0 && y < MAP_HEIGHT) {
            if (world[x][y].equals(Tileset.WALL)) {
                hub = "WALL";
            } else if (world[x][y].equals(Tileset.FLOOR)) {
                hub = "FLOOR";
            } else if(world[x][y].equals(Tileset.FLOWER)){
                hub = "FLOWER";
            }else{
                hub = "NOTHING";
            }
        }
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, MAP_HEIGHT - 1, hub);
    }
}
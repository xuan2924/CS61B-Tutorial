package byog.CoreFF;

import java.io.*;

public class GameSave implements Serializable {
    public static class UserLoad implements Serializable {
        public Room.Position pos;
        private static final long serialVersionUID = 123123123123123L;
        public int seed;

        public UserLoad(Room.Position pos, int seed) {
            this.pos = pos;
            this.seed = seed;
        }

        public UserLoad() {
            pos = new Room.Position();
            seed = 0;
        }
    }

    public static UserLoad loadWorld() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                UserLoad loadWorld = (UserLoad) os.readObject();
                os.close();
                return loadWorld;
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new UserLoad();
    }

    public static void saveWorld(UserLoad u) {
        File f = new File("./world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fo = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fo);
            os.writeObject(u);
            os.close();
            System.out.println("Save");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

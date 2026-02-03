package byog.Core;


import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
public class GameSave implements Serializable {

    public static class UserLoad implements Serializable {
        Position pos;
        long seed;

        private static final long serialVersionUID = 123123123123123L;

        public UserLoad(Position pos, long seed) {
            this.pos = pos;
            this.seed = seed;
        }

        public UserLoad() {
            pos = new Position(0, 0);
            seed = 0;
        }

    }

    public static UserLoad loadWorld() {
        File f = new File("./savefile.txt");
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
        File f = new File("./savefile.txt");
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

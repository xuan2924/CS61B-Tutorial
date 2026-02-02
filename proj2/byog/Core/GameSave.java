package byog.Core;

import byog.Core.Room;

import javax.swing.plaf.PanelUI;
import java.io.*;

public class GameSave implements Serializable {
    public static class UserLoad implements Serializable{
        public Room.Position pos;
        private int seed;

        public UserLoad(Room.Position pos,int seed){
            this.pos = pos;
            this.seed = seed;
        }

        public UserLoad(){
            pos = new Room.Position();
            seed = 0;
        }
    }

    private static UserLoad loadWorld(){
        File f = new File("./world.ser");
        if(f.exists()){
            try {
                FileInputStream fs=new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                UserLoad loadWorld=(UserLoad)os.readObject();
                os.close();
                return loadWorld;
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new UserLoad();
    }

    private static void saveWorld(UserLoad u){
        File f = new File("./world.ser");
        try{
            if(!f.exists()){
                f.createNewFile();
            }
            FileOutputStream fo = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fo);
            os.writeObject(u);
            os.close();
        }catch (){

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
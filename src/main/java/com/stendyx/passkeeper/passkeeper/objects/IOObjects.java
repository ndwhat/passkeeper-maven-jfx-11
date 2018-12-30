package com.stendyx.passkeeper.passkeeper.objects;

import com.stendyx.passkeeper.passkeeper.Main;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class IOObjects<list> {


    private static File FILE;

    private list Objects;

    public IOObjects(File file) {
        FILE = file;
        if (FILE.exists() && !FILE.isDirectory()) {
            Objects = readFile();
        } else {
            try {
                FILE.getParentFile().mkdirs();
                FILE.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage());
            }
        }
    }

  public  boolean isObjectsExists() {
        return Objects != null;
    }

  public  list getObjects() {
        return Objects;
    }

    private list readFile() {

        try (FileInputStream fis = new FileInputStream(FILE)) {
            if (fis.available() > 0) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                return (list) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

        return null;
    }


    public void writeToFile(list Objects) {

        try (FileOutputStream fout = new FileOutputStream(FILE)) {
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(Objects);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }


}

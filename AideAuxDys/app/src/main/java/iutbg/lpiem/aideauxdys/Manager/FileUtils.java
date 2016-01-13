package iutbg.lpiem.aideauxdys.Manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by iem on 13/01/16.
 */
public class FileUtils {
    public static File createFile(String text, String fileName) {
        try {
            File file = new File(fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
            writer.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }
}

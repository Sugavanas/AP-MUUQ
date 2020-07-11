package sample.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileScanner {
    //The directory where we are gonna store our txt files.
    public static final String dataDirectory = System.getProperty("user.dir") + "/data/";

    /**
     * Check if a file exists in data directory
     *
     * @param fileName Name of file.
     * @return Boolean
     */
    public static Boolean exists(String fileName) {
        return Files.exists(Paths.get(dataDirectory + fileName));
    }

    /**
     * Read the file and return a arraylist containing all lines.
     * @param fileName
     * @return
     */
    public static ArrayList<String> read(String fileName) {
        try {
            File file = new File(dataDirectory + fileName);
            Scanner sc = new Scanner(file);

            ArrayList<String> arrayList = new ArrayList<>();

            while (sc.hasNextLine()) {

                arrayList.add(sc.nextLine());
            }
            sc.close();

            return arrayList;
        } catch (Exception ex) {
            System.out.println("ERROR : " + dataDirectory + "  " + ex.getMessage());
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}

package sample.db;

import sample.objects.Finalist;

import java.util.ArrayList;

public class Finalists{
    private static ArrayList<Finalist> finalistList;
    private static String fileName = "finalists.txt";

    public static void load() {
        ArrayList<String> lines = FileScanner.read(fileName);
        finalistList = new ArrayList<>();

        //Just loop through the lines we read and put it into an array list.
        for (String line : lines) {  System.out.println(line);
            Finalist f = new Finalist(line);

            finalistList.add(f);
        }
    }

    public static Finalist getQuestion(int questionNumber) {
        return finalistList.get(questionNumber - 1);
    }

    public static ArrayList<Finalist> getAll() {
        if(finalistList == null) //if the list is not set yet, then load it first
            load();

        return finalistList;
    }

    /**
     * Just a simple function to select the finalist object by using id. Used for results form
     * @param id
     * @return
     */
    public static Finalist getByID(String id) {
       return finalistList.stream().filter(f -> f.getID().equals(id)).findFirst().orElse(null);
    }
}

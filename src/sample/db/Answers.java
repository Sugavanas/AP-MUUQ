package sample.db;

import sample.objects.Answer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Answers {
    private static String fileName = "finalresult.txt";

    public static Answer addSubmission(String id, String[] answers) {
        Answer a = new Answer(id, new ArrayList<String>(Arrays.asList(answers)));

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(FileScanner.dataDirectory + fileName, true)); //append set to true as we want to append to the file and not overwrite it.
            pw.println(a.saveString());
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR : " + FileScanner.dataDirectory + "  " + ex.getMessage());
        }

        return a;
    }

    public static ArrayList<Answer> getAll() {
        if (!FileScanner.exists(fileName))
            return new ArrayList<>();

        ArrayList<String> lines = FileScanner.read(fileName);
        ArrayList<Answer> answers = new ArrayList<>();

        //Just loop through the lines we read and put it into an array list.
        for (String line : lines) {
            Answer a = new Answer(line);
            answers.add(a);
        }
        return answers;
    }
}

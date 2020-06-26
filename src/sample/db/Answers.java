package sample.db;

import sample.objects.Answer;

import java.util.ArrayList;
import java.util.Arrays;

public class Answers {
    private static FileIO<Answer> fileIO = new FileIO<>("finalresult.txt", Answer.class);

    public static Answer addSubmission(String id, String[] answers) {
        Answer a = new Answer(id, new ArrayList<String>(Arrays.asList(answers)));
        fileIO.insert(a);
        return a;
    }

    public static ArrayList<Answer> getAll() {
        return fileIO.read();
    }
}

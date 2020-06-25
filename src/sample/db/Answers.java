package sample.db;

import sample.objects.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Answers {
    private static FileIO<Answer> fileIO = new FileIO<>("finalresult.txt", Answer.class);

    public static void addSubmission(String id, String[] answers) {
        Answer a = new Answer(id, new ArrayList<String>(Arrays.asList(answers)));
        fileIO.insert(a);
    }
}

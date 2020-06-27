package sample.db;

import sample.objects.Question;

import java.util.ArrayList;

public class Questions {
    private static ArrayList<Question> questionList;
    private static String fileName = "muuq.txt";

    public static void load() {
        ArrayList<String> lines = FileScanner.read(fileName);
        questionList = new ArrayList<>();

        //Just loop through the lines we read and put it into an array list.
        for (String line : lines) {
            Question q = new Question(line);
            questionList.add(q);
        }
    }

    public static int count(){
        return questionList.size();
    }

    public static Question getQuestion(int questionNumber) {
        return questionList.get(questionNumber - 1);
    }
}

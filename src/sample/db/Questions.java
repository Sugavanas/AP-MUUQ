package sample.db;

import sample.objects.Question;

import java.util.ArrayList;

public class Questions {
    private static FileIO<Question> fileIO = new FileIO<>("muuq.txt", Question.class);
    private static ArrayList<Question> questionList;

    /*public static FileIO<Question> getFileIO() {
        return fileIO;
    }*/

    public static void load() {
        questionList = fileIO.read();
    }

    public static int count(){
        return questionList.size();
    }

    public static Question getQuestion(int questionNumber) {
        return questionList.get(questionNumber - 1);
    }
}

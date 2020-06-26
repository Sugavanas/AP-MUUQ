package sample.objects;

import sample.db.Finalists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class Answer extends Objects {
    private String  finalistID;
    private Finalist finalist;
    private ArrayList<String> answers;
    private float correctAnswers;

    public Answer(String data) {
        super(data);
    }

    public Answer(String finalistID, ArrayList<String> answers) {
        this.finalistID = finalistID;
        this.answers = answers;
    }

    @Override
    public void loadFromString(String data) {
        try {
            String[] parts = data.split(":");
            this.finalistID = parts[0];
            this.answers = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                this.answers.add(parts[i]);
            }
        } catch (Exception ex) {
            System.out.println("Error Loading."); //TODO log error
        }
        finalist = Finalists.getFileIO().getByID(Integer.parseInt(finalistID));


    }

    @Override
    public String saveString() {
        AtomicReference<String> s = new AtomicReference<>(String.valueOf(finalistID) + ":"); //Atomic reference since we modifying with lambda
        answers.forEach(a -> { s.set( s.get() + a + ":"); });
        return s.get().substring(0, s.get().length()-1); //remove last ':'
    }

    @Override
    public String getID() {
        return finalistID;
    }

    public Finalist getFinalist() {
        return finalist;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}

package sample.objects;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;

public class Question extends Objects {
    private int type;
    private String answerOption;
    private String question;
    private String questionImage;
    private ArrayList<String> options;

    public Question(String data) {
        super(data);
    }

    /***
     * 3 types of question
     * type 1 = Text Question + Text Answer
     * type 2 = Text+Image Question + Text Answer
     * type 3 = Text question + Image Answer
     */

    @Override
    public void loadFromString(String data) {
        try {
            String[] parts = data.split(":");
            this.type = Integer.parseInt(parts[0]);
            this.answerOption = parts[1];
            this.question = parts[2];

            this.options = new ArrayList<>();
            switch(this.type) {
                case 2:
                    this.questionImage = parts[3];
                    this.options.add(0, parts[4]);
                    this.options.add(1, parts[5]);
                    this.options.add(2, parts[6]);
                    this.options.add(3, parts[7]);
                case 1:
                case 3:
                default:
                    this.options.add(0, parts[3]);
                    this.options.add(1, parts[4]);
                    this.options.add(2, parts[5]);
                    this.options.add(3, parts[6]);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Loading."); //TODO log error
        }
    }

    @Override
    public String saveString() {
        return null;
    }

    @Override
    public String getID() {
        return null;
    }

    public String getType() {
        return (this.type == 1 ? "A" : (this.type == 2 ? "B" : "C"));
    }

    public String getQuestion() {
        return this.question;
    }

    public Image getQuestionImage() {
        return new Image("images/questions/" + questionImage);
    }

    public boolean checkAnswer(String answerOption) {
        return (this.answerOption.equals(answerOption));
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public ArrayList<Image> getImageOptions() {
        ArrayList<Image> r = new ArrayList<>();
        for(String s : this.options) {
            r.add(new Image("images/questions/" + s));
        }
        return r;
    }
}

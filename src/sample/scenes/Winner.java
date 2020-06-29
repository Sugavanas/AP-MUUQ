package sample.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.db.Answers;
import sample.db.Questions;
import sample.objects.Answer;

import java.util.ArrayList;

public class Winner extends Stage {
    private Answer winner;
    private int correctAnswers, totalQuestions;

    public Winner() {
        loadWinner();

        //Add UI Code Here. Any details of winner can be accessed from winner and winner.getFinalist()

        Pane layout = new Pane();
        //layout.getChildren().addAll(resultPane);

        Scene scene = new Scene(layout, 1200, 700);
        scene.getStylesheets().add("javaFX.css");
        this.setScene(scene);
        this.setTitle("Miss Universe Ultimate Quiz");
        this.setResizable(false);
        this.show();
    }

    private void loadWinner() {
        Questions.load();
        correctAnswers = 0;
        totalQuestions = Questions.count();
        ArrayList<Answer> results = Answers.getAll();
        results.forEach(a -> {
            int score = getScore(a);
            if(score > correctAnswers) {
                correctAnswers = score;
                winner = a;
            }
        });
        System.out.println("Winner is " + winner.getFinalist().getName());
    }

    private int getScore(Answer a) {
        int score = 0;
        for (int i = 0; i < a.getAnswers().size(); i++) {
            Label label = new Label();
            if(Questions.getQuestion(i+1).checkAnswer(a.getAnswers().get(i))){
                score++;
            }
        }
        return score;
    }
}

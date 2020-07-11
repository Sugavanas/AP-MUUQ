package sample.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;
import sample.db.Answers;
import sample.db.Questions;
import sample.objects.Answer;

import java.util.ArrayList;

public class Winner extends Stage {
    private Answer winner;
    private int correctAnswers, totalQuestions;

    private Label lblInfo, lblWinner, lblWinnerID, lblWinnerScore;
    private Button btnBack, btnResult, btnAllResult;
    private Pane winnerPane;

    public Winner() {
        loadWinner();

        //Add UI Code Here. Any details of winner can be accessed from winner and winner.getFinalist()
        lblInfo = new Label("Winner is");
        lblInfo.setFont(new Font(20));
        lblInfo.setLayoutX(600 - Main.getCenterWidth(lblInfo));
        lblInfo.setLayoutY(150);

        lblWinner = new Label(winner.getFinalist().getName());
        lblWinner.setFont(new Font(40));
        lblWinner.setLayoutX(600 - Main.getCenterWidth(lblWinner));
        lblWinner.setLayoutY(200);

        lblWinnerID = new Label(winner.getFinalist().getID());
        lblWinnerID.setFont(new Font(25));
        lblWinnerID.setLayoutX(600 - Main.getCenterWidth(lblWinnerID));
        lblWinnerID.setLayoutY(250);

        lblWinnerScore = new Label(String.format("Scored %s out of %s", correctAnswers, totalQuestions));
        lblWinnerScore.setFont(new Font(20));
        lblWinnerScore.setLayoutX(600 - Main.getCenterWidth(lblWinnerScore));
        lblWinnerScore.setLayoutY(350);

        btnAllResult = new Button("All Results");
        btnAllResult.setLayoutX(490);
        btnAllResult.setLayoutY(450);
        btnAllResult.setId("btnResult");
        btnAllResult.setMinSize(100, 40);
        btnAllResult.setOnAction(e -> {
            new Result();
            this.hide();
        });

        btnResult = new Button("Winner Result");
        btnResult.setLayoutX(610);
        btnResult.setLayoutY(450);
        btnResult.setId("btnWinner");
        btnResult.setMinSize(100, 40);
        btnResult.setOnAction(e -> {
            new Result(winner);
            this.hide();
        });

        btnBack = new Button("Go Back");
        btnBack.setLayoutX(20);
        btnBack.setLayoutY(20);
        btnBack.setId("btnExit");
        btnBack.setMinSize(75, 30);
        btnBack.setOnAction(e -> {
            new Login();
            this.hide();
        });


        Pane layout = new Pane();
        layout.getChildren().addAll(lblWinner, lblInfo, lblWinnerID, lblWinnerScore);
        layout.getChildren().addAll(btnAllResult, btnResult, btnBack);

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

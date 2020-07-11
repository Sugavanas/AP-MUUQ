package sample.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import sample.Main;
import sample.db.Answers;
import sample.db.Questions;
import sample.objects.Answer;

import java.util.ArrayList;

public class Result extends Stage {

    private Pane selectionPane, resultPane;
    private VBox detailedResultPane;
    private ScrollPane detailedResultScrollPane;
    private Label scoreDetails, percentageScore;
    private Button btnBack;

    private ComboBox<Answer> resultDropDown;
    private ObservableList<Answer> answersObservableList;

    public Result() {
        loadStage();
    }

    public Result(Answer a) {
        loadStage();
        resultDropDown.setValue(a);
    }

    private void loadStage() {
        loadResults();

        resultDropDown.setLayoutX(0);
        resultDropDown.setLayoutY(0);
        resultDropDown.setPromptText("Select finalist");
        resultDropDown.setMinSize(1000, 50);

        selectionPane = new Pane();
        selectionPane.setLayoutX(100);
        selectionPane.setLayoutY(100);
        selectionPane.getChildren().addAll(resultDropDown);

        detailedResultPane = new VBox();
        detailedResultPane.setLayoutX(0);
        detailedResultPane.setLayoutY(0);

        detailedResultScrollPane = new ScrollPane();
        detailedResultScrollPane.setLayoutX(0);
        detailedResultScrollPane.setLayoutY(0);
        detailedResultScrollPane.setMinSize(400, 400);
        detailedResultScrollPane.setMaxSize(400, 400);
        detailedResultScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        detailedResultScrollPane.setContent(detailedResultPane);

        percentageScore = new Label();
        percentageScore.setLayoutX(600);
        percentageScore.setLayoutY(150);
        percentageScore.setFont(Font.font(30));

        scoreDetails = new Label();
        scoreDetails.setLayoutX(600);
        scoreDetails.setLayoutY(200);
        scoreDetails.setFont(Font.font(20));

        resultPane = new Pane();
        resultPane.setLayoutX(100);
        resultPane.setLayoutY(200);
        resultPane.getChildren().addAll(detailedResultScrollPane, scoreDetails, percentageScore);

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
        layout.getChildren().addAll(selectionPane);
        layout.getChildren().addAll(resultPane);
        layout.getChildren().addAll(btnBack);

        Scene scene = new Scene(layout, 1200, 700);
        scene.getStylesheets().add("javaFX.css");
        this.setScene(scene);
        this.setTitle("Miss Universe Ultimate Quiz");
        this.setResizable(false);
        this.show();
    }

    private void loadResults() {
        answersObservableList = FXCollections.observableArrayList(Answers.getAll());
        resultDropDown = new ComboBox(answersObservableList);

        Callback<ListView<Answer>, ListCell<Answer>> factoryResult = lv -> new ListCell<Answer>() {
            @Override
            protected void updateItem(Answer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty)
                    return;
                ImageView imageView = new ImageView(item.getFinalist().getCountryImage());
                imageView.setFitHeight(20);
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
                setText(empty ? "" : item.getFinalist().getName() + " - " + item.getID());
            }
        };

        resultDropDown.setCellFactory(factoryResult);
        resultDropDown.setButtonCell(factoryResult.call(null));

        //This is just so we can come to result page from test page and have something selected
        resultDropDown.setConverter(new StringConverter<Answer>() {
            @Override
            public String toString(Answer object) {
                if(object == null)
                    return null;
                else
                    return object.getFinalist().getName();
            }

            @Override
            public Answer fromString(String string) {
                return null;
            }
        });

        resultDropDown.valueProperty().addListener((obs, oldS, newS) -> {
            if (newS == null) {
                //TODO: Remove pane
            } else if (oldS == null || !oldS.getID().equals(newS.getID())) {
              loadResult(newS);
            }
        });
    }

    private void loadResult(Answer a) {
        Questions.load();
        ArrayList<String> fAnswers = a.getAnswers();
        detailedResultPane.getChildren().clear();
        int correctAnswers = 0;
        for (int i = 0; i < fAnswers.size(); i++) {
            Label label = new Label();
            if(Questions.getQuestion(i+1).checkAnswer(fAnswers.get(i))){
                //answer is correct
                label.setText(String.format("Question %s correct: %s", i+1, fAnswers.get(i)));
                label.setStyle("-fx-text-fill: #0dcd01;");
                correctAnswers++;
            } else {
                //answer is wrong
                label.setText(String.format("Question %s wrong. Chosen %s, correct option %s",
                        i+1,
                        fAnswers.get(i),
                        Questions.getQuestion(i+1).getAnswerOption()
                ));
                label.setStyle("-fx-text-fill: #ff0000;");
            }
            detailedResultPane.getChildren().add(label);
        }
        percentageScore.setText(String.format("%s %% score", Math.round( (Double.parseDouble(String.valueOf(correctAnswers)) / fAnswers.size()) * 100)));
        scoreDetails.setText(String.format("%s out of %s correct", correctAnswers, fAnswers.size()));
    }
}

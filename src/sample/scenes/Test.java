package sample.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sample.Main;
import sample.db.Questions;
import sample.objects.Finalist;
import sample.objects.Question;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


public class Test {
    private static Pane testPane, finalistPane, questionContainerPane, questionPane, bottomPane;

    private static Label lblFinalistName, lblFinalistID, lblTimeRemaining;
    private static ImageView ivCountryFlag;

    private static Label lblQuestionNumber, lblQuestion;

    private static ImageView ivQuestion;
    private static ToggleGroup tg;
    private static RadioButton a, b, c, d;

    private static ScrollPane questionListScrollPane;
    private static HBox questionListPane;
    private static Button btnPrevious, btnNext, btnSubmit;
    private static ArrayList<Button> questionBtnList;

    private static int currentQuestion;
    private static Question currentQuestionObject;
    private static Finalist finalist;
    private static String[] answers;
    private static int timeRemaining;
    private static Timeline timer;

    public static void loadScene(Finalist f) {
        finalist = f;
        timeRemaining = 5;
        loadQuestions();

        //Top Part
        lblFinalistName = new Label("Name: " + finalist.getName());
        lblFinalistName.setLayoutX(90);
        lblFinalistName.setLayoutY(0);
        lblFinalistName.setFont(Font.font(20));

        lblFinalistID = new Label("ID: " + finalist.getID());
        lblFinalistID.setLayoutX(90);
        lblFinalistID.setLayoutY(30);
        lblFinalistID.setFont(Font.font(15));

        ivCountryFlag = new ImageView(finalist.getCountryImage());
        ivCountryFlag.setPreserveRatio(true);
        ivCountryFlag.setFitWidth(80);

        lblTimeRemaining = new Label("5 Min 00 Sec");
        lblTimeRemaining.setLayoutX(950);
        lblTimeRemaining.setLayoutY(15);
        lblTimeRemaining.setFont(Font.font(25));

        finalistPane = new Pane();
        finalistPane.setLayoutX(0);
        finalistPane.setLayoutY(0);
        finalistPane.setMaxSize(1100, 50);
        finalistPane.setMinSize(1100, 50);
        finalistPane.getChildren().addAll(ivCountryFlag, lblFinalistName, lblFinalistID, lblTimeRemaining);

        //Bottom Part
        lblQuestionNumber = new Label("Question 1 of 25");
        lblQuestionNumber.setLayoutX(450);
        lblQuestionNumber.setLayoutY(20);

        btnPrevious = new Button("Back");
        btnPrevious.setLayoutX(0);
        btnPrevious.setLayoutY(50);
        btnPrevious.setOnAction(e -> back());
        btnPrevious.setDisable(true);

        questionListScrollPane = new ScrollPane();
        questionListScrollPane.setLayoutX(100);
        questionListScrollPane.setLayoutY(50);
        questionListScrollPane.setMinSize(200, 50);
        questionListScrollPane.setMaxSize(800, 50);
        questionListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //questionListScrollPane.setStyle("-fx-background-color: #336699;");
        questionListScrollPane.setContent(questionListPane);

        btnNext = new Button("Next");
        btnNext.setLayoutX(950);
        btnNext.setLayoutY(50);
        btnNext.setOnAction(e -> next());

        btnSubmit = new Button("Submit");
        btnSubmit.setLayoutX(1000);
        btnSubmit.setLayoutY(50);
        btnNext.setOnAction(e -> submit());

        bottomPane = new Pane();
        bottomPane.getChildren().addAll(lblQuestionNumber, btnPrevious, btnNext, btnSubmit);
        bottomPane.getChildren().addAll(questionListScrollPane);
        bottomPane.setLayoutX(50);
        bottomPane.setLayoutY(550);

        //Question pane
        lblQuestion = new Label("Question");
        lblQuestion.setLayoutX(0);
        lblQuestion.setLayoutY(0);
        lblQuestion.setFont(Font.font(20));

        ivQuestion = new ImageView();
        ivQuestion.setImage(finalist.getCountryImage());
        ivQuestion.setLayoutX(200);
        ivQuestion.setLayoutY(30);
        ivQuestion.setPreserveRatio(true);
        ivQuestion.setFitHeight(200);

        tg = new ToggleGroup();

        a = new RadioButton("Answer1");
        a.setToggleGroup(tg);
        b = new RadioButton("Answer2");
        b.setToggleGroup(tg);
        c = new RadioButton("Answer3");
        c.setToggleGroup(tg);
        d = new RadioButton("Answer4");
        d.setToggleGroup(tg);

        questionContainerPane = new Pane();
        questionContainerPane.setLayoutX(100);
        questionContainerPane.setLayoutY(100);
        questionContainerPane.getChildren().addAll(lblQuestion, ivQuestion, a, b, c, d);

        testPane = new Pane();
        testPane.resize(1100, 650);
        testPane.getChildren().addAll(finalistPane);
        testPane.getChildren().addAll(bottomPane);
        testPane.getChildren().addAll(questionContainerPane);
        testPane.setLayoutX(50);
        testPane.setLayoutY(25);

        Pane layout = new Pane();
        layout.getChildren().addAll(testPane);

        //Load the current question into view
        loadQuestion(currentQuestion);

        //set and start a timer (using timeline since modifying UI
        timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               timeRemaining--;
               int min = timeRemaining / 60;
               int sec = timeRemaining % 60;
               lblTimeRemaining.setText(String.format("%s Min %s Sec", min, sec));
            }
        }));
        timer.setOnFinished(e -> {
            //Submit function has to run on the UI thread.
            Platform.runLater(() -> submit());
        });
        timer.setCycleCount(timeRemaining);
        timer.play();

        Main.loadSceneWithCSS(new Scene(layout, 1200, 700));
    }

    public static void loadQuestion(int number) {
        //remove all styling first
        questionBtnList.stream()
                .filter(btn -> btn.getText().equals(String.valueOf(currentQuestion)))
                .findFirst()
                .ifPresent(e -> e.getStyleClass().removeAll("selected", "noanswer", "answer"));

        //Check for answer here
        if (tg.getSelectedToggle() != null) {
            String answer = "";
            if (currentQuestionObject.getType().equals("C")) {
                //Get the image's file name so we can easily compare answers. Other ways are more complicated.
                answer = (new File(((ImageView) ((RadioButton) tg.getSelectedToggle()).getGraphic()).getImage().impl_getUrl())).getName();
            } else {
                answer = ((RadioButton) tg.getSelectedToggle()).getText();
            }
            answers[currentQuestion - 1] = currentQuestionObject.getOptionKey(answer);
            questionBtnList.stream().filter(btn -> btn.getText().equals(String.valueOf(currentQuestion))).findFirst().ifPresent(e -> e.getStyleClass().add("answer"));
        } else {
            questionBtnList.stream().filter(btn -> btn.getText().equals(String.valueOf(currentQuestion))).findFirst().ifPresent(e -> e.getStyleClass().add("noanswer"));
        }

        //load the next question
        currentQuestionObject = Questions.getQuestion(number - 1);
        clearQuestion();
        lblQuestion.setText(currentQuestionObject.getQuestion());
        switch (currentQuestionObject.getType()) {
            case "A":
                loadTypeA();
                break;
            case "B":
                loadTypeB();
                break;
            case "C":
            default:
                loadTypeC();
                break;
        }
        currentQuestion = number;
        questionBtnList.stream().filter(btn -> btn.getText().equals(String.valueOf(currentQuestion))).findFirst().ifPresent(e -> e.getStyleClass().add("selected"));
        lblQuestionNumber.setText(String.format("Question %s of %s", currentQuestion, Questions.count()));

        //Select already selected answer or clear
        if (answers[currentQuestion - 1] != null) {
            System.out.println("Selected: " + answers[currentQuestion - 1]);
            switch (answers[currentQuestion - 1]) {
                case "A":
                    tg.selectToggle(a);
                    break;
                case "B":
                    tg.selectToggle(b);
                    break;
                case "C":
                    tg.selectToggle(c);
                    break;
                case "D":
                    tg.selectToggle(d);
                    break;
                default:
                    break;
            }
        } else {
            tg.selectToggle(null);
        }
    }

    public static void loadTypeA() {
        a.setText(currentQuestionObject.getOptions().get(0));
        b.setText(currentQuestionObject.getOptions().get(1));
        c.setText(currentQuestionObject.getOptions().get(2));
        d.setText(currentQuestionObject.getOptions().get(3));

        a.setLayoutX(50);
        a.setLayoutY(100);

        b.setLayoutX(50);
        b.setLayoutY(150);

        c.setLayoutX(50);
        c.setLayoutY(200);

        d.setLayoutX(50);
        d.setLayoutY(250);
    }

    public static void loadTypeB() {
        ivQuestion.setImage(currentQuestionObject.getQuestionImage());
        ivQuestion.setLayoutX(500 - Main.getCenterWidth(ivQuestion));
        ivQuestion.setVisible(true);

        a.setText(currentQuestionObject.getOptions().get(0));
        b.setText(currentQuestionObject.getOptions().get(1));
        c.setText(currentQuestionObject.getOptions().get(2));
        d.setText(currentQuestionObject.getOptions().get(3));

        a.setLayoutX(100);
        a.setLayoutY(250);

        b.setLayoutX(100);
        b.setLayoutY(300);

        c.setLayoutX(500);
        c.setLayoutY(250);

        d.setLayoutX(500);
        d.setLayoutY(300);

    }

    public static void loadTypeC() {
        ArrayList<ImageView> options = currentQuestionObject.getImageOptions();

        options.get(0).setPreserveRatio(true);
        options.get(1).setPreserveRatio(true);
        options.get(2).setPreserveRatio(true);
        options.get(3).setPreserveRatio(true);

        options.get(0).setFitHeight(170);
        options.get(1).setFitHeight(170);
        options.get(2).setFitHeight(170);
        options.get(3).setFitHeight(170);

        a.setGraphic(options.get(0));
        b.setGraphic(options.get(1));
        c.setGraphic(options.get(2));
        d.setGraphic(options.get(3));

        a.setMaxSize(400, 100);
        a.setLayoutX(100);
        a.setLayoutY(50);

        b.setMaxSize(400, 100);
        b.setLayoutX(100);
        b.setLayoutY(270);

        c.setMaxSize(400, 100);
        c.setLayoutX(500);
        c.setLayoutY(50);

        d.setMaxSize(400, 100);
        d.setLayoutX(500);
        d.setLayoutY(270);
    }

    private static void clearQuestion() {
        ivQuestion.setVisible(false);
        a.setText("");
        b.setText("");
        c.setText("");
        d.setText("");
        a.setGraphic(null);
        b.setGraphic(null);
        c.setGraphic(null);
        d.setGraphic(null);
    }

    public static void loadQuestions() {
        Questions.load();
        currentQuestion = 1;
        answers = new String[Questions.count()];
        questionBtnList = new ArrayList<>();
        for (int i = 1; i <= Questions.count(); i++) {
            Button btn = new Button(String.valueOf(i));
            btn.setMinWidth(30);
            btn.getStyleClass().add("btnQuestionNumber");
            int finalI = i; //copying i to finalI so it can be used in lambda expression
            btn.setOnAction(e -> loadQuestion(finalI));
            questionBtnList.add(btn);
        }
        questionListPane = new HBox();
        questionListPane.setSpacing(5);
        for (Button btn : questionBtnList) {
            questionListPane.getChildren().add(btn);
            System.out.println(btn.getText());
        }
        questionListPane.setMinWidth(questionListPane.getWidth());
        questionListPane.setMinHeight(questionListPane.getHeight());
    }

    private static void back() {
        loadQuestion(currentQuestion - 1);
        btnNext.setDisable(false);
        if(currentQuestion - 1 <= 0)
            btnPrevious.setDisable(true);
    }

    private static void next() {
        loadQuestion(currentQuestion + 1);
        btnPrevious.setDisable(false);
        if(currentQuestion + 1 > Questions.count())
            btnNext.setDisable(true);
    }

    private static void submit() {
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Submit?");
        alert.setHeaderText("Submit Answers");
        alert.setContentText("Are you sure you want to submit?");
        alert.getButtonTypes().setAll(buttonTypeNo,buttonTypeYes);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){

        } else {
            //Do Nothing
        }
    }
}

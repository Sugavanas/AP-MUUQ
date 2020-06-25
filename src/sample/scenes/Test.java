package sample.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import sample.Main;
import sample.db.Questions;
import sample.objects.Finalist;
import sample.objects.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class Test {
    private static Pane testPane, finalistPane, questionContainerPane, questionPane, bottomPane;

    private static Label lblFinalistName, lblFinalistID, lblTimeRemaining;
    private static ImageView ivCountryFlag;

    private static Label lblQuestionNumber, lblQuestion;

    private static ImageView ivQuestion;
    private static ToggleGroup tg;
    private static RadioButton a,b,c,d;

    private static ScrollPane questionListScrollPane;
    private static HBox questionListPane;
    private static Button btnPrevious, btnNext, btnSubmit;
    private static ArrayList<Button> questionBtnList;

    private static int currentQuestion;
    private static Finalist finalist;
    private static HashMap<Integer, String> answers;

    public static void loadScene(Finalist f) {
        finalist = f;
        loadQuestions();
        answers = new HashMap<>();

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

        finalistPane = new Pane();
        finalistPane.setLayoutX(0);
        finalistPane.setLayoutY(0);
        finalistPane.setMaxSize(1100, 50);
        finalistPane.setMinSize(1100, 50);
        finalistPane.getChildren().addAll(ivCountryFlag, lblFinalistName, lblFinalistID);

        //Bottom Part
        lblQuestionNumber = new Label("Question 1 of 25");
        lblQuestionNumber.setLayoutX(450);
        lblQuestionNumber.setLayoutY(20);

        btnPrevious = new Button("Back");
        btnPrevious.setLayoutX(0);
        btnPrevious.setLayoutY(50);

        questionListScrollPane = new ScrollPane();
        questionListScrollPane.setLayoutX(100);
        questionListScrollPane.setLayoutY(50);
        questionListScrollPane.setMaxSize(750,50);
        questionListScrollPane.setStyle("-fx-background-color: #336699;");
        questionListScrollPane.setContent(questionListPane);

        btnNext = new Button("Next");
        btnNext.setLayoutX(950);
        btnNext.setLayoutY(50);

        btnSubmit = new Button("Submit");
        btnSubmit.setLayoutX(1000);
        btnSubmit.setLayoutY(50);

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

        Main.loadSceneWithCSS(new Scene(layout, 1200, 700));
    }

    public static void loadQuestion(int number) {
        Question q = Questions.getQuestion(number);
        clearQuestion();
        lblQuestion.setText(q.getQuestion());
        switch(q.getType()) {
            case "A":
                loadTypeA(q);
                break;
            case "B":
                loadTypeB(q);
                break;
            case "C":
            default:
                loadTypeC(q);
                break;
        }
        currentQuestion = number;
    }

    public static void loadTypeA(Question q) {

    }

    public static void loadTypeB(Question q) {

    }

    public static void loadTypeC(Question q) {

    }

    private static void clearQuestion() {
        ivQuestion.setVisible(false);
    }

    public static void loadQuestions() {
        Questions.load();
        currentQuestion = 1;
        questionBtnList = new ArrayList<>();
        for(int i = 1; i <= Questions.count(); i++) {
            Button btn = new Button(String.valueOf(i));
            btn.getStyleClass().add("btnQuestionNumber");
            questionBtnList.add(btn);
        }
        questionListPane = new HBox();
        questionListPane.setSpacing(10);
        for(Button btn : questionBtnList) {
            questionListPane.getChildren().add(btn);
            System.out.println(btn.getText());
        }
        questionListPane.setMinWidth(questionListPane.getWidth());
        questionListPane.setMinHeight(questionListPane.getHeight());
    }
}

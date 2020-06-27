package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.db.Answers;
import sample.db.Finalists;
import sample.scenes.Login;
import sample.scenes.Winner;

import java.awt.*;

public class Main extends Application{
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.primaryStage = primaryStage; //set the primary stage to a static variable so we can access it again.

        primaryStage.setTitle("Miss Universe Ultimate Quiz");
        primaryStage.setResizable(false);

        Login.loadScene();
        //sample.scenes.Test.loadScene(Finalists.getFileIO().getByID(337066));
        //sample.scenes.Result.loadScene(Answers.fileIO.getByID(337066));
        //Winner.loadScene();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void loadScene(Scene scene) {
        scene.getStylesheets().add("javaFX.css");
        primaryStage.setScene(scene);
    }

    /**
     * Gets the center width (makes stuff look at exact center)
     */
    public static double getCenterWidth(Label control) {
        //Modified from: https://stackoverflow.com/questions/46641114/an-alternative-to-fontloader-computestringwidth
        final Text text = new Text(control.getText());
        text.setFont(control.getFont());
        new Scene(new Group(text));
        text.applyCss();
        return text.getLayoutBounds().getWidth() / 2;
    }

    public static double getCenterWidth(ImageView control) {
        final ImageView imageView = new ImageView(control.getImage());
        imageView.setFitWidth(control.getFitWidth());
        imageView.setFitHeight(control.getFitHeight());
        new Scene(new Group(imageView));
        imageView.applyCss();
        return imageView.getLayoutBounds().getWidth() / 2;
    }
}

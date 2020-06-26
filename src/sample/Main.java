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
        primaryStage.setTitle("Miss Universe Ultimate Quiz");
        //primaryStage.setScene(Login.loadScene(primaryStage));
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UNDECORATED);

        Main.primaryStage = primaryStage;
        sample.scenes.Test.loadScene(Finalists.getFileIO().getByID(337066));
        //sample.scenes.Result.loadScene(Answers.fileIO.getByID(337066));
        //Login.loadScene();
        //Winner.loadScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static double getCenterWidth(Label control) {
        //Modified from: https://stackoverflow.com/questions/46641114/an-alternative-to-fontloader-computestringwidth
        final Text text = new Text(control.getText());
        text.setFont(control.getFont());
        new Scene(new Group(text));
        text.applyCss();
        return text.getLayoutBounds().getWidth() / 2;
    }

    public static double getCenterWidth(ImageView control) {
        //Modified from: https://stackoverflow.com/questions/46641114/an-alternative-to-fontloader-computestringwidth
        final ImageView imageView = new ImageView(control.getImage());
        imageView.setFitWidth(control.getFitWidth());
        imageView.setFitHeight(control.getFitHeight());
        new Scene(new Group(imageView));
        imageView.applyCss();
        return imageView.getLayoutBounds().getWidth() / 2;
    }


    public static void loadSceneWithCSS(Scene scene) {
        scene.getStylesheets().add("javaFX.css");
        primaryStage.setScene(scene);
    }

    public static void loadScene(Scene scene) {
        primaryStage.setScene(scene);
    }
}

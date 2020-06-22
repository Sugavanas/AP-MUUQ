package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.scenes.Login;

public class Main extends Application{
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Miss Universe Ultimate Quiz");
        primaryStage.setScene(Login.loadScene(primaryStage));
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

    public static Scene loadCssScene(Scene scene) {
        scene.getStylesheets().add("javaFX.css");
        return scene;
    }
}

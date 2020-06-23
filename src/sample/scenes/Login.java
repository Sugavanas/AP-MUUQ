package sample.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import sample.Main;
import sample.db.Finalists;
import sample.objects.Finalist;

import java.util.*;


public class Login {

    private static Label title, instruction, nameLabel, passwordLabel, countryLabel;
    private static PasswordField password;
    private static Button btnLogin, btnExit;
    private static ImageView finalistImage;

    private static ObservableList<Finalist> finalistObservableList;
    private static ComboBox<Finalist> finalistList;

    private static Timeline loginTimeline;
    private static int loginDelay = 60;

    public static Scene loadScene(Stage primaryStage) {
        //First load all the data
        loadFinalistList();

        //Now load all UI elements into place.
        title = new Label("Miss Universe Ultimate Quiz");
        title.setFont(Font.font(30));
        title.setLayoutX(600 - Main.getCenterWidth(title));
        title.setLayoutY(100);

        instruction = new Label("Enter Login Credentials");
        instruction.setFont(Font.font(20));
        instruction.setLayoutX(600 - Main.getCenterWidth(instruction));
        instruction.setLayoutY(150);

        finalistImage = new ImageView();
        finalistImage.setFitHeight(190);
        finalistImage.setFitWidth(190);
        finalistImage.setLayoutX(500);
        finalistImage.setLayoutY(200);
        finalistImage.setPreserveRatio(false);

        countryLabel = new Label("");
        countryLabel.setLayoutX(500);
        countryLabel.setLayoutY(400);

        nameLabel = new Label("Choose Finalist:");
        nameLabel.setLayoutX(500);
        nameLabel.setLayoutY(430);

        finalistList.setMinSize(200, 30);
        finalistList.setMaxSize(200,30);
        finalistList.setLayoutX(500);
        finalistList.setLayoutY(450);
        finalistList.setPromptText("Select your name.");

        passwordLabel = new Label("Password:");
        passwordLabel.setLayoutX(500);
        passwordLabel.setLayoutY(490);


        password = new PasswordField();
        password.setLayoutX(500);
        password.setLayoutY(510);
        password.setMinSize(200, 30);
        password.setPromptText("Password");

        btnLogin = new Button("Login");
        btnLogin.setLayoutX(625);
        btnLogin.setLayoutY(560);
        btnLogin.setId("btnLogin");
        btnLogin.setMinSize(75, 30);
        btnLogin.setOnAction(e -> login(primaryStage));

        btnExit = new Button("Exit");
        btnExit.setLayoutX(500);
        btnExit.setLayoutY(560);
        btnExit.setId("btnExit");
        btnExit.setMinSize(75, 30);
        btnExit.setOnAction(e -> exit());

        Pane layout1 = new Pane();
        layout1.getChildren().addAll(title, instruction, nameLabel, passwordLabel, countryLabel);
        layout1.getChildren().addAll(finalistImage, finalistList, password);
        layout1.getChildren().addAll(btnLogin, btnExit);

        return Main.loadCssScene(new Scene(layout1, 1200, 700));
    }

    private static void loadFinalistList() {
        //https://stackoverflow.com/questions/41201043/javafx-combobox-using-object-property
        finalistObservableList = FXCollections.observableArrayList(Finalists.getFileIO().read());

        finalistList = new ComboBox(finalistObservableList);

        Callback<ListView<Finalist>, ListCell<Finalist>> factory = lv -> new ListCell<Finalist>() {
            @Override
            protected void updateItem(Finalist item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getCountry());
            }
        };

        finalistList.setCellFactory(factory);
        finalistList.setButtonCell(factory.call(null));

        finalistList.valueProperty().addListener((obs, oldS, newS) -> {
            if (newS == null) {
                //TODO: Remove image from view
            } else if (oldS == null || !oldS.getID().equals(newS.getID())) {
                finalistImage.setImage(newS.getImage());
                countryLabel.setText("Country: " + newS.getCountry());
                countryLabel.setLayoutX(600 - Main.getCenterWidth(countryLabel));
            }
        });
    }

    private static void login(Stage primaryStage) {
        if(finalistList.getValue() == null)
            return;

        String passwordTxt = password.getText();
        if(finalistList.getValue().checkPassword(passwordTxt)) {
            //MAGIC HAPPENS


            Dialog alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Loading");
            alert.setTitle("MUUQ is loading!!");
            alert.setContentText("Wait for");
            ((Stage) alert.getDialogPane().getScene().getWindow()).setOnCloseRequest(e -> {
                e.consume();
            });
            alert.getDialogPane()
                    .getButtonTypes().stream()
                    .map( alert.getDialogPane()::lookupButton )
                    .forEach(btn -> ButtonBar.setButtonUniformSize(btn, false));
            final Button skipBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            skipBtn.setDisable(true);
            skipBtn.setMinSize(100, 20);
            skipBtn.setOnAction(e -> {
                //Change forms here
            });

            loginDelay = 60;
            loginTimeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    skipBtn.setText(String.format("Wait for %s s", loginDelay));
                    loginDelay--;
                    if(loginDelay <= 0) {
                        skipBtn.setDisable(false);
                        skipBtn.setText("Start");
                        loginTimeline.stop();
                    }
                }
            }));
            loginTimeline.setCycleCount(Timeline.INDEFINITE);
            loginTimeline.play();


            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Password");
            alert.setContentText("Try Again!");
            alert.showAndWait();
        }
    }

    private static void exit() {
        System.exit(0);
    }
}

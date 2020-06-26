package sample.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import sample.Main;
import sample.db.Finalists;
import sample.objects.Finalist;


public class Login {

    private static Pane loginPane;

    private static Label title, instruction, lblName, lblPassword, lblCountry;
    private static PasswordField password;
    private static Button btnLogin, btnExit;
    private static ImageView finalistImage, countryFlag;

    private static ObservableList<Finalist> finalistObservableList;
    private static ComboBox<Finalist> finalistList;
    private static ComboBox<Finalist> countryList;

    private static MediaPlayer countryAnthem;

    private static Timeline loginTimeline;
    private static int loginDelay = 60;

    public static void loadScene() {
        //First load all the data
        loadFinalistList();

        //Now load all UI elements into place.
        title = new Label("Miss Universe Ultimate Quiz");
        title.setFont(Font.font(30));
        title.setLayoutX(150 - Main.getCenterWidth(title));
        title.setLayoutY(0);

        instruction = new Label("Enter Login Credentials");
        instruction.setFont(Font.font(20));
        instruction.setLayoutX(150 - Main.getCenterWidth(instruction));
        instruction.setLayoutY(50);

        finalistImage = new ImageView();
        finalistImage.setFitHeight(190);
        finalistImage.setFitWidth(190);
        finalistImage.setLayoutX(55);
        finalistImage.setLayoutY(100);
        finalistImage.setPreserveRatio(false);

        countryFlag = new ImageView();
        countryFlag.setFitWidth(50);
        countryFlag.setLayoutX(195);
        countryFlag.setLayoutY(100);
        countryFlag.setPreserveRatio(true);

        lblName = new Label("Choose Finalist:");
        lblName.setLayoutX(50);
        lblName.setLayoutY(310);

        finalistList.setMinSize(200, 30);
        finalistList.setMaxSize(200, 30);
        finalistList.setLayoutX(50);
        finalistList.setLayoutY(330);
        finalistList.setPromptText("Select your name.");

        lblPassword = new Label("Password:");
        lblPassword.setLayoutX(50);
        lblPassword.setLayoutY(370);

        password = new PasswordField();
        password.setLayoutX(50);
        password.setLayoutY(390);
        password.setMinSize(200, 30);
        password.setPromptText("Password");

        lblCountry = new Label("Choose Country: ");
        lblCountry.setLayoutX(50);
        lblCountry.setLayoutY(430);

        countryList.setMinSize(200, 30);
        countryList.setMaxSize(200, 30);
        countryList.setLayoutX(50);
        countryList.setLayoutY(450);
        countryList.setPromptText("Select your country.");

        btnLogin = new Button("Login");
        btnLogin.setLayoutX(225);
        btnLogin.setLayoutY(500);
        btnLogin.setId("btnLogin");
        btnLogin.setMinSize(75, 30);
        btnLogin.setOnAction(e -> login());

        btnExit = new Button("Exit");
        btnExit.setLayoutX(0);
        btnExit.setLayoutY(500);
        btnExit.setId("btnExit");
        btnExit.setMinSize(75, 30);
        btnExit.setOnAction(e -> exit());

        loginPane = new Pane();
        loginPane.resize(300, 550);
        loginPane.getChildren().addAll(title, instruction, lblName, lblPassword, lblCountry);
        loginPane.getChildren().addAll(finalistImage, countryFlag, finalistList, password, countryList);
        loginPane.getChildren().addAll(btnLogin, btnExit);
        loginPane.setLayoutX(450);
        loginPane.setLayoutY(75);

        Pane layout = new Pane();
        layout.getChildren().addAll(loginPane);

        Main.loadSceneWithCSS(new Scene(layout, 1200, 700));
    }

    private static void loadFinalistList() {
        //https://stackoverflow.com/questions/41201043/javafx-combobox-using-object-property
        finalistObservableList = FXCollections.observableArrayList(Finalists.getFileIO().read());

        finalistList = new ComboBox(finalistObservableList);
        countryList = new ComboBox(finalistObservableList);

        Callback<ListView<Finalist>, ListCell<Finalist>> factoryName = lv -> new ListCell<Finalist>() {
            @Override
            protected void updateItem(Finalist item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName() + " - " + item.getID());
            }
        };

        Callback<ListView<Finalist>, ListCell<Finalist>> factoryCountry = lv -> new ListCell<Finalist>() {
            @Override
            protected void updateItem(Finalist item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    ImageView imageView = new ImageView(item.getCountryImage());
                    imageView.setFitHeight(20);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                    setText(item.getCountry());
                }
            }
        };

        finalistList.setCellFactory(factoryName);
        finalistList.setButtonCell(factoryName.call(null));

        countryList.setCellFactory(factoryCountry);
        countryList.setButtonCell(factoryCountry.call(null));

        finalistList.valueProperty().addListener((obs, oldS, newS) -> {
            if (newS == null) {
                //TODO: Remove image from view
            } else if (oldS == null || !oldS.getID().equals(newS.getID())) {
                finalistImage.setImage(newS.getImage());
                //countryFlag.setImage(newS.getCountryImage());
            }
        });

        countryList.valueProperty().addListener((obs, oldS, newS) -> {
            if (newS == null) {
                //TODO: Remove image from view
            } else if (oldS == null || !oldS.getID().equals(newS.getID())) {
                countryFlag.setImage(newS.getCountryImage());
            }
        });
    }

    private static void login() {
        if (finalistList.getValue() == null)
            return;

        String passwordTxt = password.getText();

        if (!finalistList.getValue().getCountry().equals(countryList.getValue().getCountry())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Country");
            alert.setHeaderText("Invalid Country");
            alert.setContentText("Try Again!");
            alert.showAndWait();
        } else if (!finalistList.getValue().checkPassword(passwordTxt)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Password");
            alert.setHeaderText("Invalid Password");
            alert.setContentText("Try Again!");
            alert.showAndWait();
        } else {
            //MAGIC HAPPENS

            countryAnthem = new MediaPlayer(finalistList.getValue().getAnthem());
            countryAnthem.play();
            countryAnthem.setOnEndOfMedia(() -> {
                countryAnthem.stop();
                Test.loadScene(finalistList.getValue());
            });
            Dialog alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Loading");
            alert.setTitle("MUUQ is loading!!");
            alert.setContentText("Wait for");
            ((Stage) alert.getDialogPane().getScene().getWindow()).setOnCloseRequest(e -> {
                e.consume();
            });
            alert.getDialogPane()
                    .getButtonTypes().stream()
                    .map(alert.getDialogPane()::lookupButton)
                    .forEach(btn -> ButtonBar.setButtonUniformSize(btn, false));
            final Button skipBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            skipBtn.setDisable(true);
            skipBtn.setMinSize(100, 20);
            skipBtn.setOnAction(e -> {
                //Change forms here
                countryAnthem.stop();
                Test.loadScene(finalistList.getValue());
            });

            loginDelay = 3;
            loginTimeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    skipBtn.setText(String.format("Wait for %s s", loginDelay));
                    loginDelay--;
                    if (loginDelay <= 0) {
                        skipBtn.setDisable(false);
                        skipBtn.setText("Start");
                        loginTimeline.stop();
                    }
                }
            }));
            loginTimeline.setCycleCount(Timeline.INDEFINITE);
            loginTimeline.play();


            alert.show();
        }
    }

    private static void exit() {
        System.exit(0);
    }
}

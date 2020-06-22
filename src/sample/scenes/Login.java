package sample.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Main;
import sample.db.Finalists;
import sample.objects.Finalist;

public class Login {

    private static Label title, instruction, nameLabel, passwordLabel;
    private static TextField password;
    private static Button btnLogin, btnExit;
    private static ImageView finalistImage;

    private static ObservableList<Finalist> finalistObservableList;
    private static ComboBox<Finalist> finalistList;

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

        nameLabel = new Label("Choose Finalist:");
        nameLabel.setLayoutX(500);
        nameLabel.setLayoutY(430);

        finalistList.setMinSize(200, 30);
        finalistList.setMaxSize(200,30);
        finalistList.setLayoutX(500);
        finalistList.setLayoutY(450);

        passwordLabel = new Label("Password:");
        passwordLabel.setLayoutX(500);
        passwordLabel.setLayoutY(490);

        password = new TextField();
        password.setLayoutX(500);
        password.setLayoutY(510);
        password.setMinSize(200, 30);

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

        Pane layout1 = new Pane();
        layout1.getChildren().addAll(title, instruction, nameLabel, passwordLabel);
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
                setText(empty ? "" : item.getName() + " - " + item.getID());
            }
        };

        finalistList.setCellFactory(factory);
        finalistList.setButtonCell(factory.call(null));

        finalistList.valueProperty().addListener((obs, oldS, newS) -> {
            if (newS == null) {
                //TODO: Remove image from view
            } else if (oldS == null || !oldS.getID().equals(newS.getID())) {
                finalistImage.setImage(newS.getImage());
            }
        });
    }

    private static void login(Stage primaryStage) {
        if(finalistList.getValue() == null)
            return;

        String passwordTxt = password.getText();
        if(finalistList.getValue().checkPassword(passwordTxt)) {
            //MAGIC HAPPENS

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Password");
            alert.setContentText("Try Again!");
            alert.showAndWait();
        }
    }
}

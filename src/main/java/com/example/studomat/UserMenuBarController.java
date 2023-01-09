package com.example.studomat;
import com.example.studomat.database.Database;
import com.example.studomat.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.io.*;
import java.sql.SQLException;

public class UserMenuBarController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private Circle avatar;

    @FXML
    private SplitMenuButton buttonPredmeti;

    @FXML
    private SplitMenuButton buttonIspiti;

    @FXML
    public void initialize() throws SQLException, IOException {
        String pictureName = activeUser.getPictureName();
        InputStream avatarIs = new BufferedInputStream(new FileInputStream("src/main/resources/com/example/studomat/images/profilePictures/" + pictureName));
        Image avatarImg = new Image(avatarIs);
        avatar.setFill(new ImagePattern(avatarImg));
    }

    public void chooseFile () throws SQLException, IOException {
        File directory = new File("src/main/resources/com/example/studomat/images/profilePictures");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        fileChooser.setInitialDirectory(directory);
        fileChooser.setTitle("Molimo odaberite željenu sliku");
        File selectedFile = fileChooser.showOpenDialog(null);
        String fileName = selectedFile.getName();
        if (selectedFile.exists()) {
            Database.updatePictureUser(activeUser, fileName);
            activeUser.setPictureName(fileName);
            initialize();
        }
    }

    public void showHomeScreen() throws FileNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userHomeScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();

    }
    public void showExamsScreen() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userExamsScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showLoginScreen() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showSubjectRegistrationScreen() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userSubjectRegistrationScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showStudentInformationScreen() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userStudentInformationScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showSubjectUndoRegistrationScreen() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userSubjectUndoRegistrationScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showUndoExamsScreen() {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userUndoExamsScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showExtendSubjectButtons() {
        buttonPredmeti.show();
    }
    public void showExtendExamButtons() {
        buttonIspiti.show();
    }

}

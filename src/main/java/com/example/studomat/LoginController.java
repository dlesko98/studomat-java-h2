package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Rectangle rectangle;

    public static User activeUser;

    @FXML
    public void initialize () throws SQLException, IOException {
        activeUser = null;
        InputStream eyeIs = new BufferedInputStream(new FileInputStream("src/main/resources/com/example/studomat/images/eye2.png"));
        Image eyeImg = new Image(eyeIs);
        rectangle.setFill(new ImagePattern(eyeImg));
    }
    @FXML
    public void onEnter () throws SQLException, IOException {
        onLoginButtonClick();
    }
    @FXML
    protected void onLoginButtonClick () throws SQLException, IOException {
        int usernameError = 0;
        int passwordError = 0;
        List<User> userList = Database.getAllUsersFromDatabase();
        String userName = userNameTextField.getText();
        String password = passwordField.getText();
        StringBuilder errorMessages = new StringBuilder();

        if(userName.isEmpty()) {
            errorMessages.append("Korisničko ime ne smije ostati prazno!\n");
            usernameError = 1;
        }

        if(password.isEmpty()) {
            errorMessages.append("Zaporka ne smije ostati prazna!\n");
            passwordError = 1;
        }
        String checkStatus = Database.loginCheck(userList, userName, password);
        if (checkStatus.equals("Neuspjeh") && userName != "" && password != "") {
            errorMessages.append("Ne postoji takvo korisničko ime!\n");
            usernameError = 1;
        }
        if (checkStatus.equals("Kriva sifra")) {
            errorMessages.append("Pogrešna zaporka, pokušajte ponovno!\n");
            passwordError = 1;
        }
        if(errorMessages.isEmpty()) {
            for (User user : userList) {
                if (user.getUsername().equals(userName) && user.getPassword().equals(password)) {
                    activeUser = user;
                }
            }
            if (activeUser.getUsername().equals("admin")) {
                showHomeAdminScreen();
            }else showHomeUserScreen();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod prijave!");
            alert.setContentText(errorMessages.toString());
            if (usernameError == 1) {
                userNameTextField.setStyle("-fx-text-box-border: #a80404;");
            }
            if (passwordError == 1) {
                passwordField.setStyle("-fx-text-box-border: #a80404;");
            }
            alert.showAndWait();

        }
    }
    @FXML
    protected void onRegisterLinkClick () {
        showRegisterScreen();
    }

    public void showHomeAdminScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminHomeScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }
    public void showHomeUserScreen() {
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
    public void showRegisterScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registerScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();
    }

    @FXML
    protected void showPassword () throws SQLException, IOException {
        String password = passwordField.getText();
        passwordField.clear();
        passwordField.setPromptText(password);
    }
    @FXML
    protected void hidePassword () throws SQLException, IOException {
        String password = passwordField.getPromptText();
        passwordField.clear();
        passwordField.setText(password);
    }
}
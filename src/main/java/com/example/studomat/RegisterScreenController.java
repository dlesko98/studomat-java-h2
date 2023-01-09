package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

public class RegisterScreenController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private DatePicker dateOfBirthDatePicker;

    @FXML
    private Rectangle rectangle;

    @FXML
    public void initialize() throws FileNotFoundException {
        InputStream eyeIs = new BufferedInputStream(new FileInputStream("src/main/resources/com/example/studomat/images/eye.png"));
        Image eyeImg = new Image(eyeIs);
        rectangle.setFill(new ImagePattern(eyeImg));
    }

    @FXML
    protected void onRegisterButtonClick () throws SQLException, IOException {
        List<User> userList = Database.getAllUsersFromDatabase();
        Long id = (long) userList.size();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate dateOfBirth = dateOfBirthDatePicker.getValue();

        StringBuilder errorMessages = new StringBuilder();

        if(username.isEmpty()) {
            errorMessages.append("Korisničko ime ne smije ostati prazno!\n");
        }
        for (User user : userList) {
            if (username.equals(user.getUsername())) {
                errorMessages.append("Korisničko ime već postoji!");
            }
        }
        if(password.isEmpty()) {
            errorMessages.append("Zaporka ne smije ostati prazna!\n");
        }
        if(firstName.isEmpty()) {
            errorMessages.append("Ime ne smije ostati prazno!\n");
        }
        if(lastName.isEmpty()) {
            errorMessages.append("Prezime ne smije ostati prazno!\n");
        }
        if (dateOfBirth == null) {
            errorMessages.append("Datum rođenja ne smije ostati prazan!\n");
        }
        if (dateOfBirth != null) {
            Period period = Period.between(dateOfBirth, LocalDate.now());
            Integer yearDiff = period.getYears();
            if (yearDiff < 18) {
                errorMessages.append("Morate imati 18+ godina!\n");
            }

        }
        if(errorMessages.isEmpty()) {
            User newUser = new User(id, username, password, firstName, lastName, dateOfBirth, Collections.emptyList(), "avatar.png");
            Database.registerNewUser(newUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Registracija uspješna!");
            alert.setContentText("Nastavite dalje na prijavu!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod registracije!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
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

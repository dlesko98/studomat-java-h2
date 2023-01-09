package com.example.studomat;
import com.example.studomat.database.Database;
import com.example.studomat.model.Exam;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class AdminUpdateStudentScreenController {

    private User activeUser = LoginController.activeUser;

    private User selectedUser = AdminShowAllStudentsController.selectedUser;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private DatePicker dateOfBirthDatePicker;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TableView <Subject> subjectTableView;

    @FXML
    private TableColumn<Subject, String> idTableColumn;

    @FXML
    private TableColumn<Subject, String> nameTableColumn;

    @FXML
    private TableColumn<Subject, String> numberOfSemesterTableColumn;

    @FXML
    private TableColumn<Subject, String> numberOfECTSTableColumn;

    @FXML
    private TableView <Exam> examTableView;

    @FXML
    private TableColumn<Exam, String> idExamTableColumn;

    @FXML
    private TableColumn<Exam, String> nameExamTableColumn;

    @FXML
    private TableColumn<Exam, String> dateExamTableColumn;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Circle avatar;

    @FXML
    public void initialize() throws SQLException, IOException {
        String pictureName = selectedUser.getPictureName();
        InputStream avatarIs = new BufferedInputStream(new FileInputStream("src/main/resources/com/example/studomat/images/profilePictures/" + pictureName));
        Image avatarImg = new Image(avatarIs);
        avatar.setFill(new ImagePattern(avatarImg));
        nameTextField.setText(selectedUser.getFirstName());
        lastNameTextField.setText(selectedUser.getLastName());
        dateOfBirthDatePicker.setValue(selectedUser.getDateOfBirth());
        usernameTextField.setText(selectedUser.getUsername());
        passwordPasswordField.setText(selectedUser.getPassword());
        InputStream eyeIs = new BufferedInputStream(new FileInputStream("src/main/resources/com/example/studomat/images/eye.png"));
        Image eyeImg = new Image(eyeIs);
        rectangle.setFill(new ImagePattern(eyeImg));

        List<Subject> subjectList = Database.getAllSubjectsForUserFromDatabase(selectedUser);

        idTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });

        nameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        numberOfSemesterTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNumberOfSemester().toString());
        });
        numberOfECTSTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNumberOfECTS().toString());
        });

        ObservableList<Subject> subjectObservableList = FXCollections.observableList(subjectList);

        subjectTableView.setItems(subjectObservableList);

        List<Exam> examList = Database.getAllExamsForUserFromDatabase(selectedUser);

        idExamTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });

        nameExamTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSubjectName());
        });

        dateExamTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDate().toString());
        });


        ObservableList<Exam> examObservableList = FXCollections.observableList(examList);

        examTableView.setItems(examObservableList);
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
            Database.updatePictureUser(selectedUser, fileName);
            selectedUser.setPictureName(fileName);
            initialize();
        }
    }
    @FXML
    protected void showPassword () throws SQLException, IOException {
        String password = passwordPasswordField.getText();
        passwordPasswordField.clear();
        passwordPasswordField.setPromptText(password);
    }
    @FXML
    protected void hidePassword () throws SQLException, IOException {
        String password = passwordPasswordField.getPromptText();
        passwordPasswordField.clear();
        passwordPasswordField.setText(password);
    }
    @FXML
    protected void unregisterSubjectFromStudent () throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        Subject selectedSubject = subjectTableView.getSelectionModel().selectedItemProperty().getValue();
        if (subjectTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
            errorMessages.append("Izaberite predmet!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.removeSubjectFromUserById(selectedUser.getId().intValue(), selectedSubject.getId().intValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Ispis predmeta je uspješna!");
            alert.setContentText("Predmet uspješno ispisan!");
            alert.showAndWait();
            initialize();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod ispisa studenta s predmeta!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    }
    @FXML
    protected void unregisterExamFromStudent () throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        Exam selectedExam = examTableView.getSelectionModel().selectedItemProperty().getValue();
        if (examTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
            errorMessages.append("Izaberite ispit!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.removeExamFromUserById(selectedUser.getId().intValue(), selectedExam.getId().intValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Odjava ispita je uspješan!");
            alert.setContentText("Ispit uspješno odjavljen!");
            alert.showAndWait();
            initialize();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod odjave studenta s ispita!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    }
    @FXML
    protected void onUpdateButtonClick () throws SQLException, IOException {
        Database.updateUser(nameTextField.getText(), lastNameTextField.getText(), dateOfBirthDatePicker.getValue(),
                usernameTextField.getText(), passwordPasswordField.getText(), selectedUser.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjeh!");
        alert.setHeaderText("Promjena podataka je uspješna!");
        alert.setContentText("Podaci uspješno promijenjeni!");
        alert.showAndWait();

    }

}

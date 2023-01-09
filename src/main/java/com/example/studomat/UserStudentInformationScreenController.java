package com.example.studomat;
import com.example.studomat.database.Database;
import com.example.studomat.model.Exam;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserStudentInformationScreenController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private Label nameOfStudentLabel;

    @FXML
    private Label lastNameOfStudentLabel;

    @FXML
    private Label dateOfBirthOfStudentLabel;

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
    public void initialize() throws SQLException, IOException {
        nameOfStudentLabel.setText(activeUser.getFirstName());
        lastNameOfStudentLabel.setText(activeUser.getLastName());
        dateOfBirthOfStudentLabel.setText(activeUser.getDateOfBirth().toString());

        List<Subject> subjectList = Database.getAllSubjectsForUserFromDatabase(activeUser);

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

        List<Exam> examList = Database.getAllExamsForUserFromDatabase(activeUser);

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
}

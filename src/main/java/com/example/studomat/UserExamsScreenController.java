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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserExamsScreenController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private ChoiceBox<String> subjectNameChoiceBox;

    @FXML
    private TableView<Exam> examTableView;

    @FXML
    private TableColumn <Exam, String> idTableColumn;

    @FXML
    private TableColumn <Exam, String> subjectTableColumn;

    @FXML
    private TableColumn <Exam, String> dateTableColumn;

    @FXML
    public void initialize() throws SQLException, IOException {
        List<Subject> listOfSubjects = Database.getAllSubjectsForUserFromDatabase(activeUser);
        List<String> listOfSubjectsNames = listOfSubjects.stream().map(Subject::getName).collect(Collectors.toList());

        ObservableList<String> numberOfSemesterObservableList = FXCollections.observableList(listOfSubjectsNames);
        subjectNameChoiceBox.setItems(numberOfSemesterObservableList);
    }

    @FXML
    protected void onRefreshButtonClick() throws SQLException, IOException {
        if (!subjectNameChoiceBox.getSelectionModel().isEmpty()) {
            String subjectName = subjectNameChoiceBox.getValue();
            List<Subject> subjectList = Database.getAllSubjectsFromDatabase();
            List<Exam> examList = Database.getAllExamsFromDatabase();
            List<Exam> finalList = new java.util.ArrayList<>(Collections.emptyList());
            Subject chosenSubject = null;
            for (Subject subject : subjectList) {
                if (subject.getName().equals(subjectName)) {
                    chosenSubject = subject;
                }
            }
            Long subjectId = chosenSubject.getId();
            for (Exam exam : examList) {
                if (exam.getSubjectId() == subjectId) {
                    finalList.add(exam);
                }
            }
            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });

            subjectTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getSubjectName());
            });


            dateTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getDate().toString());
            });


            ObservableList<Exam> examObservableList = FXCollections.observableList(finalList);

            examTableView.setItems(examObservableList);

        }
    }
    @FXML
    protected void onRegisterExamButtonClick() throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        Long selectedExamId = examTableView.getSelectionModel().selectedItemProperty().getValue().getId();
        List<Exam> activeList = Database.getAllExamsForUserFromDatabase(activeUser);
            for (Exam exam : activeList) {
                if (exam.getId().equals(selectedExamId)) {
                    errorMessages.append("Ispit je već prijavljen!\n");
                }
            }
            if (examTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
                errorMessages.append("Izaberite ispit!\n");
            }

            if(errorMessages.isEmpty()) {
                Database.addNewExamToUserById(activeUser.getId().intValue(), selectedExamId.intValue());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspjeh!");
                alert.setHeaderText("Prijava ispita je uspješna!");
                alert.setContentText("Ispit uspješno prijavljen!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška!");
                alert.setHeaderText("Pogreška kod prijave ispita!");
                alert.setContentText(errorMessages.toString());

                alert.showAndWait();
            }
        }
    }

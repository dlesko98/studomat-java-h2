package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.Exam;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserSubjectUndoRegistrationScreenController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private ChoiceBox <String> subjectNameChoiceBox;

    @FXML
    private Label subjectInformations;

    @FXML
    private Label unos;

    @FXML
    public void initialize() throws SQLException, IOException {
        List<Subject> listOfSubjects = Database.getAllSubjectsForUserFromDatabase(activeUser);
        List<String> listOfSubjectsNames = listOfSubjects.stream().map(subject -> subject.getName()).collect(Collectors.toList());

        ObservableList<String> numberOfSemesterObservableList = FXCollections.observableList(listOfSubjectsNames);
        subjectNameChoiceBox.setItems(numberOfSemesterObservableList);
    }

    @FXML
    protected void onInfoButtonClick() throws SQLException, IOException {
        if (!subjectNameChoiceBox.getSelectionModel().isEmpty()) {
            String subjectName = subjectNameChoiceBox.getValue();
            List<Subject> subjectList = Database.getAllSubjectsForUserFromDatabase(activeUser);
            Subject chosenSubject = null;
            for (Subject subject : subjectList) {
                if (subject.getName().equals(subjectName)) {
                    chosenSubject = subject;
                }
            }
            subjectInformations.setText(chosenSubject.getName() + " nosi " + chosenSubject.getNumberOfECTS() + " ECTS bodova, i polaže se u " + chosenSubject.getNumberOfSemester() + ". semestru!");
        }
    }
    @FXML
    protected void onUndoRegisterButtonClick() throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        List<Subject> subjectList = Database.getAllSubjectsFromDatabase();
        String subjectName = subjectNameChoiceBox.getValue();
        Subject chosenSubject = null;

        if (activeUser.getSubjectList() != null) {
            for (Subject subject : subjectList) {
                if (subject.getName().equals(subjectName)) {
                    chosenSubject = subject;
                }
            }
            if (subjectNameChoiceBox.getSelectionModel().isEmpty()) {
                errorMessages.append("Polje predmeta ne smije ostati prazno!\n");
            }

            if(errorMessages.isEmpty()) {
                Database.removeSubjectFromUserById(activeUser.getId().intValue(), chosenSubject.getId().intValue());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Uspjeh!");
                alert.setHeaderText("Ispis predmeta uspješan!");
                alert.setContentText("Predmet uspješno ispisan!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška!");
                alert.setHeaderText("Pogreška kod ispisa predmeta!");
                alert.setContentText(errorMessages.toString());

                alert.showAndWait();
            }
        }
        subjectInformations.setText("");
        List<Exam> examList = Database.getAllExamsFromDatabase();
        List<Exam> finalList = new java.util.ArrayList<>(Collections.emptyList());
        for (Exam exam : examList) {
            if (exam.getSubjectId() == chosenSubject.getId()) {
                finalList.add(exam);
            }
        }
        for (Exam exam : finalList) {
            Database.removeExamFromUserById(activeUser.getId().intValue(), exam.getId().intValue());
        }
        initialize();
    }
}

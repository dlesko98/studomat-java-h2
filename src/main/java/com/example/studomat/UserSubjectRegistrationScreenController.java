package com.example.studomat;

import com.example.studomat.database.Database;
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
import java.util.List;
import java.util.stream.Collectors;

public class UserSubjectRegistrationScreenController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private ChoiceBox <String> subjectNameChoiceBox;

    @FXML
    private Label subjectInformations;

    @FXML
    private Label unos;

    @FXML
    public void initialize() throws SQLException, IOException {
        List<Subject> listOfSubjects = Database.getAllSubjectsFromDatabase();


        List<String> listOfSubjectsNames = listOfSubjects.stream().map(subject -> subject.getName()).collect(Collectors.toList());

        ObservableList<String> numberOfSemesterObservableList = FXCollections.observableList(listOfSubjectsNames);
        subjectNameChoiceBox.setItems(numberOfSemesterObservableList);
    }

    @FXML
    protected void onInfoButtonClick() throws SQLException, IOException {
        if (!subjectNameChoiceBox.getSelectionModel().isEmpty()) {
            String subjectName = subjectNameChoiceBox.getValue();
            List<Subject> subjectList = Database.getAllSubjectsFromDatabase();
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
    protected void onRegisterButtonClick() throws SQLException, IOException {
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
            List<Subject> activeList = Database.getAllSubjectsForUserFromDatabase(activeUser);
             for (Subject subject : activeList) {
                 if (subject.getId().equals(chosenSubject.getId())) {
                     errorMessages.append("Predmet je već upisan!\n");
                 }
             }
        if (subjectNameChoiceBox.getSelectionModel().isEmpty()) {
            errorMessages.append("Polje predmeta ne smije ostati prazno!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.addNewSubjectToUserById(activeUser.getId().intValue(), chosenSubject.getId().intValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Upis predmeta uspješan!");
            alert.setContentText("Predmet uspješno upisan!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod upisa predmeta!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    }
    }
}

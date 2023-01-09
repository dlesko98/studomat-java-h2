package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminInsertNewSubjectController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField numberOfECTSTextField;

    @FXML
    private ChoiceBox<Integer> numberOfSemesterChoiceBox;


    @FXML
    public void initialize() {
        List<Integer> listOfSemesters = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            listOfSemesters.add(i + 1);
        }
        ObservableList<Integer> numberOfSemesterObservableList = FXCollections.observableList(listOfSemesters);
        numberOfSemesterChoiceBox.setItems(numberOfSemesterObservableList);
    }
    @FXML
    protected void onInsertButtonClick () throws SQLException, IOException {
        String name = nameTextField.getText();
        String numberOfECTS = numberOfECTSTextField.getText();
        Integer numberOfSemester = 0;
        StringBuilder errorMessages = new StringBuilder();
        List<Subject> subjectList = Database.getAllSubjectsFromDatabase();
        Long id = null;
        if (subjectList.size() > 0) {
            id = (long) subjectList.get(subjectList.size() - 1).getId() + 1;
        }else {
            id = 0L;
        }

        if(name.isEmpty()) {
            errorMessages.append("Ime predmeta ne smije ostati prazno!\n");
        }
        for (Subject subject : subjectList) {
            if (name.equals(subject.getName())) {
                errorMessages.append("Ime predmeta već postoji!\n");
            }
        }
        if(numberOfECTS.isEmpty()) {
            errorMessages.append("Broj ECTS-a ne smije ostati prazno!\n");
        }else {
            try {
                Integer numberOfECTSInteger = Integer.valueOf(numberOfECTS);
                if (Integer.parseInt(numberOfECTS) < 1 || Integer.parseInt(numberOfECTS) > 9) {
                    errorMessages.append("Broj ECTS-a ne smije biti manji od 1, ili veći od 10!\n");
                }
            }
            catch (NumberFormatException ex) {
                errorMessages.append("Broj ECTS-a mora biti u brojčanom formatu!\n");
            }
        }

        if (numberOfSemesterChoiceBox.getSelectionModel().isEmpty()) {
            errorMessages.append("Polje semestar ne smije ostati prazan!\n");
        }else {
            numberOfSemester = numberOfSemesterChoiceBox.getValue();
        }
        if(errorMessages.isEmpty()) {
            Subject newSubject = new Subject(id, name, Integer.valueOf(numberOfECTS), numberOfSemester);
            Database.insertNewSubject(newSubject);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Dodavanje predmeta uspješno!");
            alert.setContentText("Predmet uspješno dodan!");
            alert.showAndWait();
            initialize();
            nameTextField.setText("");
            numberOfECTSTextField.setText("");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod dodavanja predmeta!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }

}

package com.example.studomat;
import com.example.studomat.database.Database;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminUpdateSubjectScreenController {

    private User activeUser = LoginController.activeUser;

    private Subject selectedSubject = AdminShowAllSubjectController.selectedSubject;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField numberOfECTSTextField;

    @FXML
    private ChoiceBox<Integer> numberOfSemesterChoiceBox;


    @FXML
    public void initialize() throws SQLException, IOException {
        List<Integer> listOfSemesters = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            listOfSemesters.add(i + 1);
        }
        ObservableList<Integer> numberOfSemesterObservableList = FXCollections.observableList(listOfSemesters);
        numberOfSemesterChoiceBox.setItems(numberOfSemesterObservableList);
        nameTextField.setText(selectedSubject.getName());
        numberOfECTSTextField.setText(selectedSubject.getNumberOfECTS().toString());
        numberOfSemesterChoiceBox.setValue(selectedSubject.getNumberOfSemester());

    }
    public void onUpdateButtonClick () throws SQLException, IOException {
        Database.updateSubject(nameTextField.getText(), numberOfECTSTextField.getText(), numberOfSemesterChoiceBox.getValue().toString(), selectedSubject.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjeh!");
        alert.setHeaderText("Promjena podataka je uspješna!");
        alert.setContentText("Podaci uspješno promijenjeni!");
        alert.showAndWait();

    }
}
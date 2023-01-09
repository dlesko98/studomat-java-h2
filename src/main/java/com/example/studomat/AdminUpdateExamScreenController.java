package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.Exam;
import com.example.studomat.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;

public class AdminUpdateExamScreenController {

    private User activeUser = LoginController.activeUser;

    private Exam selectedExam = AdminShowAllExamsController.selectedExam;

    @FXML
    private DatePicker dateOfExamDatePicker;


    @FXML
    public void initialize() throws SQLException, IOException {
        dateOfExamDatePicker.setValue(selectedExam.getDate());
    }
    @FXML
    protected void onUpdateButtonClick () throws SQLException, IOException {
        Database.updateExam(dateOfExamDatePicker.getValue(), selectedExam.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjeh!");
        alert.setHeaderText("Promjena podataka je uspješna!");
        alert.setContentText("Podaci uspješno promijenjeni!");
        alert.showAndWait();
    }
}

package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.Exam;
import com.example.studomat.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserUndoExamsScreenController {

    private User activeUser = LoginController.activeUser;

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
            List<Exam> examList = Database.getAllExamsForUserFromDatabase(activeUser);

            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });

            subjectTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getSubjectName());
            });


            dateTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getDate().toString());
            });


            ObservableList<Exam> examObservableList = FXCollections.observableList(examList);

            examTableView.setItems(examObservableList);
    }

    @FXML
    protected void onUndoRegisterExamButtonClick() throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        Long selectedExamId = examTableView.getSelectionModel().selectedItemProperty().getValue().getId();

        if (examTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
            errorMessages.append("Izaberite ispit!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.removeExamFromUserById(activeUser.getId().intValue(), selectedExamId.intValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Odjava ispita je uspješna!");
            alert.setContentText("Ispit uspješno odjavljen!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod odjave ispita!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    initialize();
    }
}

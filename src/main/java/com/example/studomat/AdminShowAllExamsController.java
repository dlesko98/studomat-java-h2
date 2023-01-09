package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.Exam;
import com.example.studomat.model.Subject;
import com.example.studomat.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminShowAllExamsController {

    private User activeUser = LoginController.activeUser;


    @FXML
    private TableView <Exam> examTableView;

    @FXML
    private TableColumn<Exam, String> idTableColumn;

    @FXML
    private TableColumn<Exam, String> subjectTableColumn;

    @FXML
    private TableColumn<Exam, String> dateTableColumn;

    @FXML
    private ChoiceBox<String> subjectNameChoiceBox;

    public static Exam selectedExam;


    @FXML
    public void initialize() throws SQLException, IOException {

        List<Subject> listOfSubjects = Database.getAllSubjectsFromDatabase();
        List<String> listOfSubjectsNames = listOfSubjects.stream().map(Subject::getName).collect(Collectors.toList());
        listOfSubjectsNames.add(0, "Svi");


        ObservableList<String> subjectsNameObservableList = FXCollections.observableList(listOfSubjectsNames);
        subjectNameChoiceBox.setItems(subjectsNameObservableList);

        List<Exam> examList = Database.getAllExamsFromDatabase();

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
    protected void onSearchButtonClick() throws SQLException, IOException {
        List<Exam> examList = Database.getAllExamsFromDatabase();
        List<Exam> filteredList = new ArrayList<>(examList);

        if (!subjectNameChoiceBox.getSelectionModel().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(s -> s.getSubjectName().equals(subjectNameChoiceBox.getValue()))
                    .collect(Collectors.toList());

        }
        if (!subjectNameChoiceBox.getSelectionModel().isEmpty() && subjectNameChoiceBox.getValue().equals("Svi")) {
            filteredList = examList;
        }
        examTableView.setItems(FXCollections.observableList(filteredList));

    }
    @FXML
    protected void onDeleteExamButtonClick () throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        Exam selectedExam = examTableView.getSelectionModel().selectedItemProperty().getValue();
        if (examTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
            errorMessages.append("Izaberite ispit za brisanje!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.deleteExam(selectedExam);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Brisanje ispita je uspješno odrađeno!");
            alert.setContentText("Ispit uspješno izbrisan!");
            alert.showAndWait();
            initialize();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod brisanja ispita!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }
    @FXML
    protected void checkSelectedExam () throws SQLException, IOException {
        selectedExam = examTableView.getSelectionModel().selectedItemProperty().getValue();
        if (selectedExam != null) {
            onUpdateExamButtonClick();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod odabira ispita!");
            alert.setContentText("Molim vas odaberite ispit kojemu želite promijeniti podatke!");

            alert.showAndWait();
        }
    }
    @FXML
    protected void onUpdateExamButtonClick () throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminUpdateExamScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().setScene(scene);
        Main.getStage().show();

    }

}

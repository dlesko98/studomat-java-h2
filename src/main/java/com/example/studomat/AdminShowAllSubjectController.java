package com.example.studomat;

import com.example.studomat.database.Database;
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

public class AdminShowAllSubjectController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private ChoiceBox<String> numberOfSemesterChoiceBox;

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

    public static Subject selectedSubject;

    @FXML
    public void initialize() throws SQLException, IOException {
        List<String> listOfSemesters = new ArrayList<>();
        listOfSemesters.add("svi");
        for (int i = 0; i < 6; i++) {
            listOfSemesters.add(String.valueOf(i + 1));
        }
        ObservableList<String> numberOfSemesterObservableList = FXCollections.observableList(listOfSemesters);
        numberOfSemesterChoiceBox.setItems(numberOfSemesterObservableList);

        List<Subject> subjectList = Database.getAllSubjectsFromDatabase();

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
    }
    @FXML
    protected void onSearchButtonClick() throws SQLException, IOException {
        List<Subject> subjectList = Database.getAllSubjectsFromDatabase();
        List<Subject> filteredList = new ArrayList<>(subjectList);

        if (!numberOfSemesterChoiceBox.getSelectionModel().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(s -> s.getNumberOfSemester().toString().equals(numberOfSemesterChoiceBox.getValue()))
                    .collect(Collectors.toList());

        }
        if (!numberOfSemesterChoiceBox.getSelectionModel().isEmpty() && numberOfSemesterChoiceBox.getValue().equals("svi")) {
            filteredList = subjectList;
        }
        subjectTableView.setItems(FXCollections.observableList(filteredList));

    }
    @FXML
    protected void onDeleteSubjectButtonClick () throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        Subject selectedSubject = subjectTableView.getSelectionModel().selectedItemProperty().getValue();
        if (subjectTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
            errorMessages.append("Izaberite predmet za brisanje!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.deleteSubject(selectedSubject);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Brisanje predmeta je uspješno odrađeno!");
            alert.setContentText("Predmet uspješno izbrisan!");
            alert.showAndWait();
            initialize();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod brisanja predmeta!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }
    @FXML
    protected void checkSelectedSubject () throws SQLException, IOException {
        selectedSubject = subjectTableView.getSelectionModel().selectedItemProperty().getValue();
        if (selectedSubject != null) {
            onUpdateSubjectButtonClick();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod odabira predmeta!");
            alert.setContentText("Molim vas odaberite predmet kojem želite promijeniti podatke!");

            alert.showAndWait();
        }
    }
    @FXML
    protected void onUpdateSubjectButtonClick () throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminUpdateSubjectScreen.fxml"));
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

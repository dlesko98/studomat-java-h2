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
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AdminInsertNewExamController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private DatePicker dateOfExamDatePicker;

    @FXML
    private ChoiceBox<String> nameOfSubjectChoiceBox;


    @FXML
    public void initialize() throws SQLException, IOException {
        List<Subject> listOfSubjects = Database.getAllSubjectsFromDatabase();
        List<String> listOfSubjectsNames = listOfSubjects.stream().map(Subject::getName).collect(Collectors.toList());


        ObservableList<String> subjectsNameObservableList = FXCollections.observableList(listOfSubjectsNames);
        nameOfSubjectChoiceBox.setItems(subjectsNameObservableList);
    }
    @FXML
    protected void onInsertButtonClick () throws SQLException, IOException {
        LocalDate dateOfExam = dateOfExamDatePicker.getValue();
        String subjectName = nameOfSubjectChoiceBox.getValue();
        StringBuilder errorMessages = new StringBuilder();
        List<Exam> examList = Database.getAllExamsFromDatabase();
        List<Subject> subjectList = Database.getAllSubjectsFromDatabase();
        Long id = null;
        if (examList.size() > 0) {
            id = (long) examList.get(examList.size() - 1).getId() + 1;
        }else {
            id = 0L;
        }



        for (Exam exam : examList) {
            if (exam.getDate().equals(dateOfExam) && exam.getSubjectName().equals(subjectName)) {
                errorMessages.append("Ispit za ovaj predmet već postoji na odabrani dan!\n");
            }
        }
        if(dateOfExam == null) {
            errorMessages.append("Niste odabrali datum ispita!\n");
        }else {
            if(dateOfExam.isBefore(LocalDate.now())) {
                errorMessages.append("Morate odabrati buduće vrijeme!\n");
            }
        }
        if(subjectName == null) {
            errorMessages.append("Niste odabrali niti jedan predmet!\n");
        }

        if(errorMessages.isEmpty()) {
            Subject selectedSubject = null;
            for (Subject subject : subjectList) {
                if (subject.getName() == subjectName) {
                    selectedSubject = subject;
                }
            }
            Exam newExam = new Exam(id, dateOfExam, selectedSubject.getId(), selectedSubject.getName());
            Database.insertNewExam(newExam);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Dodavanje ispita je uspješno!");
            alert.setContentText("Ispit uspješno dodan!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod dodavanja ispita!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }
    }

}

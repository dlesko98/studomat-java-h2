package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminShowAllStudentsController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private TableView <User> userTableView;

    @FXML
    private TableColumn<User, String> idTableColumn;

    @FXML
    private TableColumn<User, String> nameTableColumn;

    @FXML
    private TableColumn<User, String> lastNameTableColumn;

    @FXML
    private TableColumn<User, String> dateOfBirthTableColumn;

    @FXML
    private TableColumn<User, String> subjectsTableColumn;

    public static User selectedUser;

    @FXML
    public void initialize() throws SQLException, IOException {
        List<User> listOfUsers = Database.getAllUsersFromDatabase();

        listOfUsers.remove(0);

        idTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });

        nameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getFirstName());
        });

        lastNameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLastName());
        });

        dateOfBirthTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDateOfBirth().toString());
        });

        subjectsTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getSubjectList().toString());
        });

        ObservableList<User> userObservableList = FXCollections.observableList(listOfUsers);

        userTableView.setItems(userObservableList);
    }
    @FXML
    protected void onDeleteStudentButtonClick () throws SQLException, IOException {
        StringBuilder errorMessages = new StringBuilder();
        User selectedUser = userTableView.getSelectionModel().selectedItemProperty().getValue();
        if (userTableView.getSelectionModel().selectedItemProperty().getValue() == null) {
            errorMessages.append("Izaberite studenta za brisanje!\n");
        }

        if(errorMessages.isEmpty()) {
            Database.deleteUser(selectedUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspjeh!");
            alert.setHeaderText("Brisanje studenta je uspješno odrađeno!");
            alert.setContentText("Student uspješno izbrisan!");
            alert.showAndWait();
            initialize();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod brisanja studenta!");
            alert.setContentText(errorMessages.toString());

            alert.showAndWait();
        }

    }
    @FXML
    protected void checkSelectedUser () throws SQLException, IOException {
        selectedUser = userTableView.getSelectionModel().selectedItemProperty().getValue();
        if (selectedUser != null) {
            onUpdateStudentButtonClick();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška!");
            alert.setHeaderText("Pogreška kod odabira studenta!");
            alert.setContentText("Molim vas odaberite studenta kojemu želite promijeniti podatke!");

            alert.showAndWait();
        }
    }
    @FXML
    protected void onUpdateStudentButtonClick () throws SQLException, IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminUpdateStudentScreen.fxml"));
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

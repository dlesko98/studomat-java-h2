package com.example.studomat;

import com.example.studomat.database.Database;
import com.example.studomat.model.News;
import com.example.studomat.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminHomeScreenController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private TextArea text1Field;

    @FXML
    private TextArea text2Field;

    @FXML
    public void initialize() throws SQLException, IOException {
        List<News> newsList = Database.getAllNewsFromDatabase();
        text1Field.setText((newsList.get(0).getText()));
        text2Field.setText((newsList.get(1).getText()));
    }
    @FXML
    protected void onSaveButtonClick() throws SQLException, IOException {
        String text1 = text1Field.getText();
        String text2 = text2Field.getText();
        Database.updateNews(text1, 0L);
        Database.updateNews(text2, 1L);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjeh!");
        alert.setHeaderText("Promijene su pohranjene!");
        alert.setContentText("Promijene su uspješno spremljene, nastavite dalje s radom!");
        alert.showAndWait();
    }

}

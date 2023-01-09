package com.example.studomat;
import com.example.studomat.database.Database;
import com.example.studomat.model.News;
import com.example.studomat.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserHomeScreenController {

    private User activeUser = LoginController.activeUser;

    @FXML
    private TextArea text1;

    @FXML
    private TextArea text2;


    @FXML
    public void initialize() throws SQLException, IOException {
        List<News> newsList = Database.getAllNewsFromDatabase();
        text1.setText((newsList.get(0).getText()));
        text2.setText((newsList.get(1).getText()));
    }
}

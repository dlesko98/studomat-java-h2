package com.example.studomat;

import com.example.studomat.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Studomat");
        InputStream logoIs = new BufferedInputStream(new FileInputStream("src/main/resources/com/example/studomat/images/studomat.png"));
        Image logoImg = new Image(logoIs);
        stage.getIcons().add(logoImg);
        stage.setScene(scene);
        stage.show();
        Database.createTable();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return mainStage;
    }
}
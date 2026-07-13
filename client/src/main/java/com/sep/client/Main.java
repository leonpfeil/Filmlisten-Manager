package com.sep.client;

import com.sep.client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("loginView.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(fxmlloader.load()));
        primaryStage.show();
    }
}

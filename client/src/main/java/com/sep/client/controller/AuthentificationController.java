package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AuthentificationController {

    @FXML
    private Button backToLoginButton;

    @FXML
    private TextField codeTextfield;

    @FXML
    private Button doneButton;

    @FXML
    private Button sendCodeButton;

    private String username;
    public void initialize() {

    }
    @FXML
    public void onBackToLoginButtonEvent(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/loginView.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(fxmlLoader.load()));
            ((Stage) doneButton.getScene().getWindow()).close();
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDoneButtonEvent(ActionEvent event) {
        startUserView();
    }

    @FXML
    public void onEnterKeyPressed(KeyEvent event) {
        startUserView();
    }

    private void startUserView() {
        if (HttpRequests.getCodeFromAuth(this.username).substring(0, 4).equals(codeTextfield.getText())) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/userView.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                UserController userController = fxmlLoader.getController();
                userController.setUsernameForOverlay(this.username, HttpRequests.checkIfUserIsAdmin("/users/isAdmin", this.username));

                Stage authentificationStage = (Stage) doneButton.getScene().getWindow();
                Stage userStage = new Stage();
                userStage.setScene(new Scene(root));
                authentificationStage.close();
                userStage.show();
                HttpRequests.dropAuthTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alerts.giveWrongCodeAlert(codeTextfield);
        }
    }
    @FXML
    private void onSendCodeButtonEvent(ActionEvent event) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                HttpRequests.sendEmail("/email/send", getEmailFromUsername());
                timer.cancel();
            }
        }, 0, 1);

        Alerts.giveEmailSuccessAlert();
    }

    //Methode um die einem Username zugehörige Email zu bekommen
    private String getEmailFromUsername() {
        return HttpRequests.getString(this.username, "/users/getEmail");
    }

    //Methode um den im LoginController eingegebenen Username zu übergeben
    public void getEnteredUsername(String username) {
        this.username = username;
    }
}



package com.sep.client.controller;

import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ProfileController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Button changeProfileButton, userStatisticButton;

    @FXML
    private Button securitySettingsButton;

    @FXML
    private ImageView profilePicImageView;

    private String username;
    private File myPFP;
    private Image image;
    private String baseImage;


    @FXML
    public void onSecuritySettingsButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/securitySettingsView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        SecuritySettingsController securitySettingsController = fxmlLoader.getController();
        securitySettingsController.setUsernameAndTwoFACheckbox(this.username, HttpRequests.checkIfTwoFAEnabled(this.username));

        Stage securitySettingsStage = new Stage();
        securitySettingsStage.setScene(new Scene(root));
        securitySettingsStage.show();
    }

    @FXML
    private  void onUploadPfpButtonEvent(ActionEvent event){
       baseImage= ShowDataController.chooseImage(myPFP,image,baseImage,profilePicImageView);

    }

    @FXML
    private void onConfirm(ActionEvent event){
        try {

            HttpRequests.postString("/users/choosePFP",username,baseImage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
        usernameLabel.setText(username);
        baseImage= HttpRequests.getString(username,"/users/pfp");
        ShowDataController.showBaseImage(username,"/users/pfp", profilePicImageView);
    }

    public void onUserStatisticButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/userStatisticView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        UserStatisticController statisticController = fxmlLoader.getController();
        statisticController.setUsername(this.username);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

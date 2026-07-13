package com.sep.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ShowHBoxDataController {
    @FXML
    private ImageView imageImageView;

    @FXML
    private Button linkButton;

    @FXML
    private Label titleLabel;

    private String name;
    private UserController userController;

    public void setData(String name,String urlPath,Boolean isMovie){
       this.name=name;
        if(name!=null) {
            if(isMovie){
               linkButton.setText("Show Movie");
        }
            ShowDataController.showBaseImage(name, urlPath, imageImageView);
            titleLabel.setText(name);
        }
    }
    @FXML
    public void onLinkButtonAction(ActionEvent event){
        userController.searchForElement(name,linkButton.getText());
    }
    public void setUserController(UserController userController){
        this.userController=userController;
    }
}

package com.sep.client.controller;

import com.sep.client.extras.HttpRequests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class UserProfileListController {

    @FXML
    private VBox vBoxElements;
    @FXML
    private Label listLabel;
    private String username;
    List<String> listOfElementsNames= new ArrayList<>();
    private Stage thisStage;
    private final UserController userController;

    public UserProfileListController(UserController userController, String fxmlURL) {
        this.userController = userController;
        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlURL));

            // Set this class as the controller
            loader.setController(this);

            // Load the scene
            thisStage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public void setUsername(String username,String urlPath){
        this.username=username;
        String elementsNames = HttpRequests.getString(username,urlPath);
        listOfElementsNames= Stream.of((elementsNames.split(",#,"))).collect(Collectors.toList());
        switch (urlPath) {
                case "/usersProfile/watchlist":
                    showElements(listOfElementsNames, vBoxElements, "/movie/banner", true);
                    listLabel.setText("Watchlist");
                    break;
                case "/usersProfile/friends":
                    showElements(listOfElementsNames, vBoxElements, "/users/pfp", false);
                    listLabel.setText("Freunde");
                    break;
                case "/usersProfile/alreadyWatchedlist":
                    showElements(listOfElementsNames, vBoxElements, "/movie/banner", true);
                    listLabel.setText("Gesehene Filme");
                    break;
                default:
                    System.out.println("Fehler");
                    break;
            }

    }

    private void showElements(List<String> lists,VBox vBox,String urlPath,Boolean isMovie){
        System.out.println(lists);

        if(!listOfElementsNames.get(0).equals("")) {
            try {
                for (String element : lists) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/sep/client/showHBoxDataView.fxml"));
                    HBox hBox = fxmlLoader.load();
                    ShowHBoxDataController showHBoxDataController = fxmlLoader.getController();
                    showHBoxDataController.setData(element, urlPath, isMovie);
                    showHBoxDataController.setUserController(userController);
                    vBox.getChildren().add(hBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}

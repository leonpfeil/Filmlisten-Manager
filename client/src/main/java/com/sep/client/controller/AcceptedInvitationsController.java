package com.sep.client.controller;

import com.sep.client.extras.HttpRequests;
import com.sep.client.model.MovieInvitation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.Locale;

public class AcceptedInvitationsController {
    private ObservableList<MovieInvitation> doneInvitationsList = FXCollections.observableArrayList();

    @FXML
    private Button backButton;

    @FXML
    private TableView<MovieInvitation> table;

    @FXML
    private TableColumn<MovieInvitation, String> dateCol, messageCol, movieNameCol, timeCol, usernameCol;

    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        movieNameCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        table.setItems(doneInvitationsList);

        HttpResponse<String> response = HttpRequests.get(UserController.username,"/movieInvitation/getAllByDone");
        JSONArray doneInvitations = new JSONArray(response.body());

        for(int i=0; i<doneInvitations.length(); i++) {
            JSONObject invitation = (JSONObject) doneInvitations.get(i);

            String sender = invitation.getString("sender");
            String target = invitation.getString("target");
            String movieName = invitation.getString("moviename");
            String date = invitation.getString("date");
            String time = invitation.getString("time");
            String message = invitation.getString("text");

            doneInvitationsList.add(new MovieInvitation(sender, target, movieName, date, time, message));
        }
    }

    @FXML
    public void onBackButtonEvent(ActionEvent event) {
        ((Stage) backButton.getScene().getWindow()).close();
    }
}


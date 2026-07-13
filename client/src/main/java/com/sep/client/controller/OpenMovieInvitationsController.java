package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import com.sep.client.model.MovieInvitation;
import com.sep.client.model.Report;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

public class OpenMovieInvitationsController {
    private ObservableList<MovieInvitation> invitationsList = FXCollections.observableArrayList();

    @FXML
    private Button backButton, acceptButton, declineButton, acceptedInvitationsButton;

    @FXML
    private TableView<MovieInvitation> invitationTable;

    @FXML
    private TableColumn<MovieInvitation, String> usernameCol, movieNameCol, dateCol, timeCol, messageCol;

    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        movieNameCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        invitationTable.setItems(invitationsList);

        HttpResponse<String> response = HttpRequests.get(UserController.username, "/movieInvitation/getAllByNotDone");
        JSONArray invitations = new JSONArray(response.body());

        for(int i=0; i<invitations.length(); i++){
            JSONObject invitation = (JSONObject) invitations.get(i);

            String sender = invitation.getString("sender");
            String target = invitation.getString("target");
            String movieName = invitation.getString("moviename");
            String date = invitation.getString("date");
            String time = invitation.getString("time");
            String message = invitation.getString("text");

            invitationsList.add(new MovieInvitation(sender, target, movieName, date, time, message));
        }
    }

    @FXML
    public void onAcceptButtonAction(ActionEvent event) throws IOException, InterruptedException {
        MovieInvitation movieInvitation = invitationTable.getSelectionModel().getSelectedItem();
        String movieName = movieInvitation.getMovieName();
        String sender = movieInvitation.getSender();
        String target = movieInvitation.getTarget();

        HttpResponse<String> response = HttpRequests.getIsMovieInWatchlist("/movie/isMovieInWatchlist", UserController.username, HttpRequests.replaceIlleagalCharacters(movieName));
        if (response.body().equals("false")) {
            HttpRequests.postUsernameAndMoviename("/movie/addToWatchlist", HttpRequests.replaceIlleagalCharacters(movieName), UserController.username);
        } else {

        }
        invitationsList.remove(movieInvitation);

        HttpRequests.changeMovieInvitationToDone(sender, target, movieName);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                HttpRequests.sendMovieInvitationAcceptedEmail(HttpRequests.getString(sender, "/users/getEmail"));
                timer.cancel();
            }
        }, 0, 1);

        Alerts.movieInvitationAccepted();
    }

    @FXML
    public void onBackButtonAction(ActionEvent event) {
        ((Stage) backButton.getScene().getWindow()).close();
    }

    @FXML
    public void onDeclineButtonEvent(ActionEvent event) {
        MovieInvitation movieInvitation = invitationTable.getSelectionModel().getSelectedItem();
        String sender = movieInvitation.getSender();
        String target = movieInvitation.getTarget();
        String movieName = movieInvitation.getMovieName();

        HttpRequests.deleteFromMovieInvitation(sender, target, HttpRequests.replaceIlleagalCharacters(movieName));
        invitationsList.remove(movieInvitation);

        Alerts.movieInvitationDeclined();
    }

    @FXML
    public void onAcceptedInvitationsButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/acceptedInvitationsView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        Stage acceptedInvitationsStage = new Stage();
        acceptedInvitationsStage.setScene(new Scene(root));
        acceptedInvitationsStage.show();
    }
}


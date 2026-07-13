package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SecuritySettingsController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Button doneButton, backToProfileButton;

    @FXML
    private Button friendListPrivateButton, friendListOnlyFansButton, friendListPublicButton;

    @FXML
    private Button watchedListPrivateButton, watchedListOnlyFansButton, watchedListPublicButton;

    @FXML
    private Button watchListPrivateButton, watchListOnlyFansButton, watchListPublicButton;

    @FXML
    private Button reviewListPrivateButton, reviewListOnlyFansButton, reviewListPublicButton;

    @FXML
    private Label friendListLabel, watchedListLabel, watchListLabel, reviewListLabel;

    @FXML
    private CheckBox twoFACheckBox;

    private String username;

    public void initialize() {

    }

    @FXML
    public void onDoneButtonEvent(ActionEvent event) {
        if (twoFACheckBox.isSelected()) {
            if (!HttpRequests.checkIfTwoFAEnabled(this.username)) {
                HttpRequests.enableTwoFA(this.username);
                Alerts.twoFaEnabled();

                ((Stage) doneButton.getScene().getWindow()).close();
            } else {
                ((Stage) doneButton.getScene().getWindow()).close();
            }
        } else {
            if (HttpRequests.checkIfTwoFAEnabled(this.username)) {
                HttpRequests.disableTwoFA(this.username);
                Alerts.twoFaDisabled();

                ((Stage) doneButton.getScene().getWindow()).close();
            } else {
                ((Stage) doneButton.getScene().getWindow()).close();
            }
        }
    }

    @FXML
    public void onBackToProfileButtonEvent(ActionEvent event) {
        ((Stage) backToProfileButton.getScene().getWindow()).close();
    }

    //Friendlist Button Events
    @FXML
    public void onFriendListOnlyFansButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setFriendListOnlyFans");
        Alerts.onlyFansAlert();
    }

    @FXML
    public void onFriendListPrivateButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setFriendListPrivate");
        Alerts.privateAlert();
    }

    @FXML
    public void onFriendListPublicButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setFriendListPublic");
        Alerts.publicAlert();
    }

    //Watchedlist Button Events
    @FXML
    public void onWatchedListOnlyFansButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setWatchedListOnlyFans");
        Alerts.onlyFansAlert();
    }

    @FXML
    public void onWatchedListPrivateButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setWatchedListPrivate");
        Alerts.privateAlert();
    }

    @FXML
    public void onWatchedListPublicButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setWatchedListPublic");
        Alerts.publicAlert();
    }

    //Watchlist Button Events
    @FXML
    public void onWatchListOnlyFansButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setWatchListOnlyFans");
        Alerts.onlyFansAlert();
    }

    @FXML
    public void onWatchListPrivateButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setWatchListPrivate");
        Alerts.privateAlert();
    }

    @FXML
    public void onWatchListPublicButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setWatchListPublic");
        Alerts.publicAlert();
    }

    //Reviewlist Button Events
    @FXML
    public void onReviewListOnlyFansButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setReviewListOnlyFans");
        Alerts.onlyFansAlert();
    }

    @FXML
    public void onReviewListPrivateButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setReviewListPrivate");
        Alerts.privateAlert();
    }

    @FXML
    public void onReviewListPublicButtonEvent(ActionEvent event) {
        HttpRequests.setPrivacySettings(this.username, "/privacy/setReviewListPublic");
        Alerts.publicAlert();
    }

    public void setUsernameAndTwoFACheckbox(String username, Boolean twoFA) {
        this.username = username;

        if (twoFA == true) {
            twoFACheckBox.setSelected(true);
        } else {
            twoFACheckBox.setSelected(false);
        }

        if (HttpRequests.getListPrivacyStatus("/privacy/getFriendListPrivacyStatus", username).equals("0")) {
            friendListLabel.setText("Öffentlich");
            friendListPublicButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getFriendListPrivacyStatus", username).equals("1")) {
            friendListLabel.setText("Freunde");
            friendListOnlyFansButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getFriendListPrivacyStatus", username).equals("2")) {
            friendListLabel.setText("Niemanden");
            friendListPrivateButton.setDisable(true);
        }

        if (HttpRequests.getListPrivacyStatus("/privacy/getReviewListPrivacyStatus",username).equals("0")) {
            reviewListLabel.setText("Öffentlich");
            reviewListPublicButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getReviewListPrivacyStatus", username).equals("1")) {
            reviewListLabel.setText("Freunde");
            reviewListOnlyFansButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getReviewListPrivacyStatus", username).equals("2")) {
            reviewListLabel.setText("Niemanden");
            reviewListPrivateButton.setDisable(true);
        }

        if (HttpRequests.getListPrivacyStatus("/privacy/getWatchedListPrivacyStatus", username).equals("0")) {
            watchedListLabel.setText("Öffentlich");
            watchedListPublicButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchedListPrivacyStatus", username).equals("1")) {
            watchedListLabel.setText("Freunde");
            watchedListOnlyFansButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchedListPrivacyStatus", username).equals("2")) {
            watchedListLabel.setText("Niemanden");
            watchedListPrivateButton.setDisable(true);
        }

        if (HttpRequests.getListPrivacyStatus("/privacy/getWatchListPrivacyStatus", username).equals("0")) {
            watchListLabel.setText("Öffentlich");
            watchListPublicButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchListPrivacyStatus", username).equals("1")) {
            watchListLabel.setText("Freunde");
            watchListOnlyFansButton.setDisable(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchListPrivacyStatus", username).equals("2")) {
            watchListLabel.setText("Niemanden");
            watchListPrivateButton.setDisable(true);
        }

        usernameLabel.setText("Sicherheitseinstellungen für: "+username);
    }
}

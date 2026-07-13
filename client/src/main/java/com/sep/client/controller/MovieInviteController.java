package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MovieInviteController {

    @FXML
    private Button backButton, sendInviteButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField filmNameTextField, hourTextField, minuteTextField;

    @FXML
    private TextArea textArea;

    private String sender, target;

    @FXML
    public void onBackButtonEvent(ActionEvent event) {
        ((Stage) backButton.getScene().getWindow()).close();
    }

    @FXML
    public void onSendInviteButtonEvent(ActionEvent event) throws IOException, InterruptedException {
        if (sender.equals(target)) {
            Alerts.sameUsernameAlert();

            ((Stage) backButton.getScene().getWindow()).close();
        } else {
            String movieName = filmNameTextField.getText();
            String time = hourTextField.getText()+":"+minuteTextField.getText();
            String text;

            if (textArea.getText().equals("")) {
                text = "null";
            }

            text = textArea.getText();

            String format = "yyyy-MM-dd";
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern(format);

            datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
            LocalDate dateValue = datePicker.getValue();
            String date = dateValue.toString();

            if (errorAlert(filmNameTextField, hourTextField, minuteTextField) && dateAlert(datePicker.getValue())) {
                HttpRequests.post("/movieInvitation/add", HttpRequests.createMovieInviteJSONObject(movieName, time, text, date, sender, target, false));
                Alerts.sendFilmInviteSuccessAlert(sender, target, HttpRequests.replaceIlleagalCharacters(movieName), HttpRequests.replaceIlleagalCharacters(date), HttpRequests.replaceIlleagalCharacters(time));
                HttpRequests.postStringsByURL("/movieInvitation/addRequest?requested=" + target + "&requester=" + sender);

                ((Stage) backButton.getScene().getWindow()).close();
            }
        }
    }

    private Boolean errorAlert(TextField nameTextField, TextField hourTextField, TextField minuteTextField) {
        Boolean bool = true;

        if (filmNameTextField.getText().equals("")) {
            bool = false;

            Alerts.filmNameEmptyErrorAlert();
        } else if (datePicker.getValue()==null) {
            bool = false;

            Alerts.datePickerEmptyAlert();
        } else if (hourTextField.getText().equals("") || minuteTextField.getText().equals("")) {
            bool = false;

            Alerts.timeEmptyAlert();
        } else if (HttpRequests.get(HttpRequests.replaceIlleagalCharacters(filmNameTextField.getText()), "/movie/existsMovie").statusCode()!=302) {
            bool = false;

            Alerts.filmNotFoundErrorAlert();
            nameTextField.clear();
        } else if (hourTextField.getText().matches("[0-9]+") && minuteTextField.getText().matches("[0-9]+")) {
            if (hourTextField.getText().length()>2 || hourTextField.getText().length()<2 || Integer.parseInt(hourTextField.getText())>24 || minuteTextField.getText().length()>2 || minuteTextField.getText().length()>2 || Integer.parseInt(minuteTextField.getText())>60) {
                bool = false;

                Alerts.timeErrorAlert();
                hourTextField.clear();
                minuteTextField.clear();
            }
        } else if (hourTextField.getText().matches("[a-zA-Z]+") || minuteTextField.getText().matches("[a-zA-Z]+")) {
            bool = false;

            Alerts.timeErrorAlert();
            hourTextField.clear();
            minuteTextField.clear();
        }

        return bool;
    }

    private Boolean dateAlert(LocalDate date) {
        Boolean bool = true;
        LocalDate now = LocalDate.now();

        if (date.isBefore(now)) {
            bool = false;

            Alerts.wrongDateAlert();
        }

        return bool;
    }

    public void getEnteredUsername(String username, String selectedUser) {
        this.sender = username;
        this.target = selectedUser;
    }
}

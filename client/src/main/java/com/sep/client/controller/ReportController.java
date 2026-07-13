package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class ReportController {

    private String username;

    @FXML
    private Button sendReport, cancel;
    @FXML
    private TextArea reportText;
    @FXML
    private Label movieLabel;

    public void initialize(){

    }

    public void onSendReportButton(){
        JSONObject json = new JSONObject();
        json.put("username",username);
        json.put("reportMessage",reportText.getText());

        System.out.println(HttpRequests.replaceIlleagalCharacters(movieLabel.getText()));

        HttpResponse<String> response =HttpRequests.postWithReqParam("/report/add", json, HttpRequests.replaceIlleagalCharacters(movieLabel.getText()));
        System.out.println(response.statusCode());

        if(response.statusCode()!=200){Alerts.reportFailedAlert();}
        else{
            Alerts.reportSentAlert();
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        }
    }

    public void onCancelButton(ActionEvent actionEvent) {
        ((Stage) cancel.getScene().getWindow()).close();
    }

    public void getMovieLabelAndUsername(String movieName, String username){
        movieLabel.setText(movieName);
        this.username=username;
    }

}

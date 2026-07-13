package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.util.Base64Util;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;

public class groupCreationController {
    @FXML
    TextArea descriptionArea;
    @FXML
    TextField nameField;
    @FXML
    CheckBox isPrivateCheckbox;
    @FXML
    Button createButton;

    @FXML
    public void onCreateButtonEvent() throws Exception{
        JSONObject json = new JSONObject();
        json.put("groupname", nameField.getText());
        json.put("requester", UserController.username);
        json.put("description", descriptionArea.getText());
        json.put("isPrivate", isPrivateCheckbox.isSelected());

        System.out.println(json.toString());
        HttpResponse<String> test = HttpRequests.post("/group/createGroup",json);

        if(test.statusCode() == HttpStatus.NOT_ACCEPTABLE.value())
        {
            Alerts.genericAlert("Gruppe existiert bereits", "Bitte wählen sie einen anderen Gruppennahmen", Alert.AlertType.ERROR);
        }

        ((Stage) descriptionArea.getScene().getWindow()).close();
        groupChatOverviewController.updateTable();
    }
}

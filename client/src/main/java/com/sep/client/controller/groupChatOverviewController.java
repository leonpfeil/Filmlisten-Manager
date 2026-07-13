package com.sep.client.controller;
import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import com.sep.client.model.GroupChat;
import com.sep.client.model.MovieInvitation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.sep.client.controller.UserController.username;

public class groupChatOverviewController {
    private static ObservableList<GroupChat> groupList = FXCollections.observableArrayList();
    static List<GroupChat> bufferedList = new ArrayList<>();                                    //Speicher liste damit nicht jedesmal eine neue Anfrage gemacht werden muss
    @FXML
    private Button BackButton, CreateGroupButton, JoinButton;

    @FXML
    private TableView<GroupChat> groupTable;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<GroupChat, String> groupnameCol, participantCol, descriptionCol;
    public void initialize() {
        groupnameCol.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        participantCol.setCellValueFactory(new PropertyValueFactory<>("participants"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        groupTable.setItems(groupList);

        //handle doubleclicks   https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
        groupTable.setRowFactory( tv -> {
            TableRow<GroupChat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    GroupChat rowData = row.getItem();
                    openChat(rowData);
                }
            });
            return row ;
        });


        updateTable();
    }


    @FXML
    public void onBackButtonEvent() {
        ((Stage) BackButton.getScene().getWindow()).close();
    }

    @FXML
    public void onJoinButtonEvent() throws Exception{
        GroupChat groupChat = groupTable.getSelectionModel().getSelectedItem();
        String groupName = groupChat.getGroupName();

        JSONObject json = new JSONObject();
        json.put("groupName", groupName);
        json.put("requester",username);

        //HttpRequests.postStringsByURL("/group/joinGroup?group=" + groupName +"&requester=" + username);
        HttpRequests.post("/group/joinGroup", json);

        updateTable();
    }

    @FXML
    public void onCreateGroupButton() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/groupCreationView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        Stage groupChatOverview = new Stage();
        groupChatOverview.setScene(new Scene(root));
        groupChatOverview.show();

    }

    public static void updateTable() //einträge werden alphabetisch sortiert. vll ändern
    {
        String url = "http://localhost:8080/group/getGroups?requester=" + username;

        //https://stackoverflow.com/questions/23151306/how-to-retrieve-list-string-from-httpresponse-object-in-java
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<GroupChat>> groupChatEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupChat>>() {}, Collections.emptyMap() ) ;
        if (groupChatEntity.getStatusCode() == HttpStatus.OK) {
            bufferedList.clear();
            bufferedList.addAll(groupChatEntity.getBody());

            groupList.clear();
            groupList.addAll(bufferedList);
        }
    }

    void openChat(GroupChat groupChat)
    {
        if(groupChat.getParticipants().contains(username))
        {
            try
            {
                java.awt.Desktop.getDesktop().browse(new URI("http://localhost:8080/?username=" + username + "&username2=" + groupChat.getGroupName().replace(" ","%20") + "&group=1"));
            }
            catch (Exception e)
            {
                System.out.println("Failed to start Groupsession");
            }
        }
        else
        {
            Alerts.genericAlert("Ungültige Gruppe", "Sie müssen der Gruppe erst beitreten bevor sie am Chat teilnehmen können", Alert.AlertType.ERROR);
        }


    }

    public void onSearchFieldKeyEntered()
    {
        String searchString = searchField.getText();
        List<GroupChat> copyOfBufferedList = bufferedList;

        copyOfBufferedList = copyOfBufferedList.stream().filter(element -> element.getGroupName().contains(searchString)).collect(Collectors.toList());
        groupList.clear();
        groupList.addAll(copyOfBufferedList);
    }


}

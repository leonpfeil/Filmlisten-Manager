package com.sep.client.controller;

import com.sep.client.extras.HttpRequests;
import com.sep.client.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.sep.client.model.Report;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//JavaFX Controller Fenster für die Mainpage
public class OpenReportsController {

    private ObservableList<Report> reportList = FXCollections.observableArrayList();


    @FXML
    private Button closeMainpageButton, reportErledigt;
    @FXML
    public TableView<Report> table;
    @FXML
    public TableColumn<Report, String> reportIDCol;
    @FXML
    public TableColumn<Report, String> movieNameCol;
    @FXML
    public TableColumn<Report, String> reportMessageCol;
    @FXML
    public TableColumn<Report, String> userCol;

    public OpenReportsController() {

    }

    @FXML
    public void initialize() {

        reportIDCol.setCellValueFactory(new PropertyValueFactory<>("reportID"));
        movieNameCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        reportMessageCol.setCellValueFactory(new PropertyValueFactory<>("reportString"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("user"));

        table.setItems(reportList);

        HttpResponse<String> response = HttpRequests.get("", "/report/getAll");
        JSONArray reports = new JSONArray(response.body());

        for(int i=0; i<reports.length(); i++){
            JSONObject report = (JSONObject) reports.get(i);

            String reportID = String.valueOf(report.getLong("reportID"));
            String movieName =report.getString("movieName");
            String reportString = report.getString("reportMessage");
            String user = report.getString("username");

            reportList.add(new Report(
                    reportID,
                    movieName,
                    reportString,
                    user));
        }
    }


    @FXML
    void onReportErledigtButton(ActionEvent event) {
        JSONObject json = new JSONObject();
        Report report = table.getSelectionModel().getSelectedItem();

        Long reportID = Long.parseLong(report.getReportID());
        json.put("reportID", reportID);

        try {
            HttpResponse<String> response = HttpRequests.post("/report/done",json);
            if(response.statusCode()==200){
                reportList.remove(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onCloseButtonEvent(ActionEvent event) {
        ((Stage) closeMainpageButton.getScene().getWindow()).close();
    }

    public void onChangeButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/changeDatabaseView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        ChangeDatabaseController changeDatabaseController = fxmlLoader.getController();
        changeDatabaseController.changeMovie(table.getSelectionModel().getSelectedItem().getMovieName());
    }
}
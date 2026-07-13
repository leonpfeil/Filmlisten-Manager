package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class FilterController {
    @FXML
    private Button doneButtonFilter;
    @FXML
    private TextField yearTextfieldFilter, filmlengthTextfieldFilter, regisseurTextfieldFilter, directorTextfieldFilter;
    @FXML
    private TextArea castTextareaFilter;
    @FXML
    private MenuButton genreMenubuttonFilter;
    @FXML
    private CheckBox adventureCBFilter, actionCBFilter, animationCBFilter, dokuCBFilter, dramaCBFilter, eroticCBFilter, familyCBFilter, fantasyCBFilter,
            horrorCBFilter, comedyCBFilter, crimeCBFilter, loveCBFilter, musicCBFilter, scifiCBFilter, otherCBFilter, thrillerCBFilter, westernCBFilter;

    private String username;
    ArrayList<CheckBox> checkboxes = new ArrayList<>();

    public void initialize() {
        checkboxes.add(adventureCBFilter);
        checkboxes.add(actionCBFilter);
        checkboxes.add(animationCBFilter);
        checkboxes.add(dokuCBFilter);
        checkboxes.add(dramaCBFilter);
        checkboxes.add(eroticCBFilter);
        checkboxes.add(familyCBFilter);
        checkboxes.add(fantasyCBFilter);
        checkboxes.add(horrorCBFilter);
        checkboxes.add(comedyCBFilter);
        checkboxes.add(crimeCBFilter);
        checkboxes.add(loveCBFilter);
        checkboxes.add(musicCBFilter);
        checkboxes.add(scifiCBFilter);
        checkboxes.add(otherCBFilter);
        checkboxes.add(thrillerCBFilter);
        checkboxes.add(westernCBFilter);
    }
    public void onDoneButtonFilterEvent() throws IOException, InterruptedException {
        String filmlength = replaceIlleagalCharacters(filmlengthTextfieldFilter.getText());
        String releaseYear = replaceIlleagalCharacters(yearTextfieldFilter.getText());
        String regisseur = replaceIlleagalCharacters(regisseurTextfieldFilter.getText());
        String director = replaceIlleagalCharacters(directorTextfieldFilter.getText());
        String cast = replaceIlleagalCharacters(castTextareaFilter.getText());
        String category="";

        for(CheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                category = category + checkbox.getText() + ", ";
            }
        }
        if(!category.equals("")) {
            category = category.substring(0, category.length()-2);
        }
        category = replaceIlleagalCharacters(category);
        HttpRequests.postFilter("/movie/setFilter", this.username, filmlength, releaseYear, regisseur, director, cast, category);
        Alerts.selectedFiltersAlert();
        ((Stage) doneButtonFilter.getScene().getWindow()).close();
    }

    public void setUsernameAndFilters(String username){
        this.username=username;
        HttpResponse<String> response = HttpRequests.get(username, "/movie/getFilters");
        List<String> filterliste = new ArrayList<>();
        if (response.body() != null && !response.body().equals("[]")) {
            JSONArray filterarray = new JSONArray(response.body());
            filterliste = filterarray.toList().stream().map(Object::toString).toList();
            String filmlengthFilter = filterliste.get(0);
            String releaseYearFilter = filterliste.get(1);
            String regisseurFilter = filterliste.get(2);
            String directorFilter = filterliste.get(3);
            String castFilter = filterliste.get(4);
            String categoryFilter = filterliste.get(5);
            filmlengthTextfieldFilter.setText(filmlengthFilter);
            yearTextfieldFilter.setText(releaseYearFilter);
            regisseurTextfieldFilter.setText(regisseurFilter);
            directorTextfieldFilter.setText(directorFilter);
            castTextareaFilter.setText(castFilter);
            for(CheckBox box : checkboxes) {
                if(categoryFilter.contains(box.getText())) {
                    box.setSelected(true);
                }
                else {
                    box.setSelected(false);
                }
            }
        }
    }

    private String replaceIlleagalCharacters(String string){
        return string.replaceAll(" ", "%20")
                .replaceAll("!","%21")
                .replaceAll("\"","%22")
                .replaceAll("#","%23")
                .replaceAll("\\$","%24")
                .replaceAll("&","%26")
                .replaceAll("'","%27%27")
                .replaceAll("\\(","%28")
                .replaceAll("\\)","%29")
                .replaceAll("\\*","%2A")
                .replaceAll("\\+","%2B")
                .replaceAll(",","%2C")
                .replaceAll("-","%2D")
                .replaceAll("/","%2F")
                .replaceAll(":","%3A")
                .replaceAll(";","%3B")
                .replaceAll("<","%3C")
                .replaceAll("=","%3D")
                .replaceAll(">","%3F")
                .replaceAll("\\?","%40")
                .replaceAll("@","%5B")
                .replaceAll("\\[","%5C")
                //.replaceAll("\","%5D") ?
                .replaceAll("]","%7B")
                .replaceAll("\\{","%7C")
                .replaceAll("}","%7D");
    }

}

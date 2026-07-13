package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateMovieController {

    @FXML
    private Button backToMainpageButton, deleteratingbutton;

    @FXML
    private Label movienamelabel, ratinglabel;

    @FXML
    private CheckBox CBonestar, CBtwostars, CBthreestars, CBfourstars, CBfivestars;

    private String moviename, username, ratingname;

    @FXML
    private TextField ratingcaption;

    @FXML
    private TextArea ratingtext;

    @FXML
    void onBackToMainpageButtonEvent(ActionEvent event) {
        ((Stage) backToMainpageButton.getScene().getWindow()).close();
    }

    //Filmbewertung erstellen und globales Rating berechnen
    //Film wird pauschal mit 1 Stern bewertet, wenn keine Sterneanzahl ausgewählt wurde
    @FXML
    public void rateMovie(){

        JSONObject jsonO = new JSONObject();
        int rating = getRating();


        jsonO.put("ratingname", username+movienamelabel.getText());
        jsonO.put("username", username);
        jsonO.put("movieName", movienamelabel.getText());
        jsonO.put("movieSeen", true);
        jsonO.put("rating", rating);
        jsonO.put("ratingCaption", ratingcaption.getText());
        jsonO.put("ratingText", ratingtext.getText());

        try {
            HttpRequests.post("/ratemovie/add", jsonO);
            ((Stage) backToMainpageButton.getScene().getWindow()).close();
            Alerts.ratingSuccessfull();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //-------

        calculateGlobal();

    }

    @FXML
    public void deleteRating(){

        JSONObject jsonO = new JSONObject();
        jsonO.put("ratingname", username+movienamelabel.getText());
        jsonO.put("username", username);
        jsonO.put("movieName", moviename);
        jsonO.put("movieSeen", true);
        jsonO.put("rating", 0);
        jsonO.put("ratingCaption", "");
        jsonO.put("ratingText", "");

        try {
            HttpRequests.post("/ratemovie/add", jsonO);
            ((Stage) backToMainpageButton.getScene().getWindow()).close();
            Alerts.ratingDeleted();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //-------

        calculateGlobal();

    }

    public void calculateGlobal(){
        String remove = replaceIlleagalCharacters(moviename);
        JSONArray a = new JSONArray(HttpRequests.get(remove,"/ratemovie/getglobal").body());
        List<String> global = a.toList().stream().map(Object::toString).toList();

        int grating = 0;
        int count = 0;
        float globalrating = 0;


        try {
            for (int i = 0; i < a.length(); i++) {
                int z = Integer.parseInt(global.get(i));
                grating = grating + z;
                count++;
            }

            globalrating = 0;

            if(count != 0) {

                globalrating = (float) grating / count;
                globalrating = (float) (Math.round(globalrating*10)/10.0);
            }

        }

        catch(Exception e){
            e.printStackTrace();
            System.out.println("Berechnung des globalen Rating fehlgeschlagen");
        }

        JSONObject jsonO = new JSONObject();
        jsonO.put("movieName", movienamelabel.getText());
        jsonO.put("globalrating", globalrating);

        try {
            HttpRequests.post("/movie/setglobal", jsonO);
            ((Stage) backToMainpageButton.getScene().getWindow()).close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Sterne abfragen
    int getRating(){

        int rating = 1;

        try {

            for (CheckBox checkbox : checkboxes) {
                if (checkbox.isSelected() == true) {
                    String a = checkbox.getText();
                    rating = Integer.parseInt(a);
                }
            }
        }
        catch (Exception e){

        }

        return rating;
    }


    ArrayList<CheckBox> checkboxes = new ArrayList<>();
    public void initialize() {

        checkboxes.add(CBonestar);
        checkboxes.add(CBtwostars);
        checkboxes.add(CBthreestars);
        checkboxes.add(CBfourstars);
        checkboxes.add(CBfivestars);
    }



    public void setRatinglabel(String rating){
        ratinglabel.setText(rating);
    }

    public void setRatingcaption(String caption){
        ratingcaption.setText(caption);
    }

    public void setRatingtext(String text){
        ratingtext.setText(text);
    }


    public void setUsername(String username){

        this.username = username;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
        movienamelabel.setText(moviename);
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


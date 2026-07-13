package com.sep.client.controller;

import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.ArrayList;


//JavaFX Controller Klasse für das Scraping Fenster
public class ScrapingController {

    @FXML
    private Button backToMainpageButton;
    @FXML
    private Button scrapingbutton;
    @FXML
    private Button stopscrapingbutton;
    @FXML
    private TextField filmanzahl;

    @FXML
    private DatePicker startdate;
    @FXML
    private DatePicker enddate;

    @FXML
    private CheckBox actionCB, adventureCB, animationCB, biographyCB, comedyCB, crimeCB, documentaryCB, dramaCB, familyCB, fantasyCB, historyCB, horrorCB,
            musicCB, mysteryCB, romanceCB, scifiCB, sportCB, thrillerCB, warCB, westernCB;

    @FXML
    void urlAnzeigen(){

        //mindestens ein Genre wurde ausgewählt
        if(genrePicked() == true){
            String genre = pasteGenre();
            //beide Datepicker leer
            if (startdate.getValue() == null && enddate.getValue() == null){
                System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short"+"&genres="+genre);
            }
            //ein Datepicker leer
            else if(startdate.getValue() == null || enddate.getValue() == null){
                if(enddate.getValue() == null){
                    System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue()+"&genres="+genre);
                }
                else if(startdate.getValue() == null){
                    System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=," + enddate.getValue()+"&genres="+genre);
                }

            }
            //beide Datepicker wurden genutzt
            else if(startdate.getValue() != null && enddate.getValue() != null){
                System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue() + "," + enddate.getValue()+"&genres="+genre);
            }
        }
        //kein Genre wurde ausgewählt
        else if(genrePicked() == false){
            //ein Datepicker ist leer
            if (startdate.getValue() == null && enddate.getValue() == null){
                System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short");
            }
            else if(startdate.getValue() == null || enddate.getValue() == null){
                if(enddate.getValue() == null){
                    System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue());
                }
                else{
                    System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=," + enddate.getValue());
                }
            }
            else if(startdate.getValue() != null & enddate.getValue() != null){
                System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue() + "," + enddate.getValue());
            }
            else{
                System.out.println("https://www.imdb.com/search/title/?title_type=feature,tv_movie,short");
            }
        }
    }

    @FXML
    //Obergrenze auslesen
    int getNumber() {

        try {

            String a = filmanzahl.getText();
            int number = Integer.parseInt(a);

            return number;

        }
        catch (Exception e) {
        }
        return 0;
    }




    // Download starten
    @FXML
    void scrapingFilter(){

        urlAnzeigen();

        JSONObject jsonObject = new JSONObject();
        int number = getNumber();                       //Obergrenze

        System.out.println(number + " Filme werden heruntergeladen...");
        //mindestens ein Genre wurde ausgewählt
        if(genrePicked() == true){
            String genre = pasteGenre();
            //beide Datepicker leer
            if (startdate.getValue() == null && enddate.getValue() == null){
                jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short"+"&genres="+genre);
                jsonObject.put("Obergrenze", number);
            }
            //ein Datepicker leer
            else if(startdate.getValue() == null || enddate.getValue() == null){
                if(enddate.getValue() == null){
                    jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue()+"&genres="+genre);
                    jsonObject.put("Obergrenze", number);
                }
                else if(startdate.getValue() == null){
                    jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=," + enddate.getValue()+"&genres="+genre);
                    jsonObject.put("Obergrenze", number);
                }

            }
            //beide Datepicker wurden genutzt
            else if(startdate.getValue() != null && enddate.getValue() != null){
                jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue() + "," + enddate.getValue()+"&genres="+genre);
                jsonObject.put("Obergrenze", number);
            }
        }
        //kein Genre wurde ausgewählt
        else if(genrePicked() == false){
            //ein Datepicker ist leer
            if (startdate.getValue() == null && enddate.getValue() == null){
                jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short");
                jsonObject.put("Obergrenze", number);
            }
            else if(startdate.getValue() == null || enddate.getValue() == null){
                if(enddate.getValue() == null){
                    jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue());
                    jsonObject.put("Obergrenze", number);
                }
                else{
                    jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=," + enddate.getValue());
                    jsonObject.put("Obergrenze", number);
                }
            }
            else if(startdate.getValue() != null & enddate.getValue() != null){
                jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&release_date=" + startdate.getValue() + "," + enddate.getValue());
                jsonObject.put("Obergrenze", number);
            }
            else{
                jsonObject.put("URL", "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short");
                jsonObject.put("Obergrenze", number);
            }
        }


        try {
            HttpRequests.post("/scraping/start", jsonObject);
        } catch (Exception e) {

        }
    }


    // Methode zum prüfen, ob eine Checkbox gewählt wurde
    boolean genrePicked(){

        for(CheckBox checkbox : checkboxes) {
            if(checkbox.isSelected()) {
                return true;
            }
        }
        return false;
    }



    //Methode um die Checkboxauswahl einzufügen
    String pasteGenre(){

        String genreurl = "";

        for(CheckBox checkbox : checkboxes) {
            if(checkbox.isSelected()) {
                if(genreurl == ""){
                    String temp = checkbox.getText();
                    genreurl = temp;
                }
                else {
                    String temp = checkbox.getText();
                    genreurl = genreurl + "," + temp;
                }
            }
        }
        return genreurl;
    }




    //zurück zur Hauptseite
    @FXML
    void onBackToMainpageButtonEvent(ActionEvent event) {
        ((Stage) backToMainpageButton.getScene().getWindow()).close();
    }




    ArrayList<CheckBox> checkboxes = new ArrayList<>();

    public void initialize() {
        checkboxes.add(actionCB);
        checkboxes.add(adventureCB);
        checkboxes.add(animationCB);
        checkboxes.add(biographyCB);
        checkboxes.add(comedyCB);
        checkboxes.add(crimeCB);
        checkboxes.add(documentaryCB);
        checkboxes.add(dramaCB);
        checkboxes.add(familyCB);
        checkboxes.add(fantasyCB);
        checkboxes.add(historyCB);
        checkboxes.add(horrorCB);
        checkboxes.add(musicCB);
        checkboxes.add(mysteryCB);
        checkboxes.add(romanceCB);
        checkboxes.add(scifiCB);
        checkboxes.add(sportCB);
        checkboxes.add(thrillerCB);
        checkboxes.add(warCB);
        checkboxes.add(westernCB);
    }
}

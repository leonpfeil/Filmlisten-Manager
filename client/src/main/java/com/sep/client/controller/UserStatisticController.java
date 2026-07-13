package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

public class UserStatisticController {
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private BarChart barChart;
    @FXML
    private Label watchedMinutesLabel, minutenLabel, filmNameLabel;
    @FXML
    private Button applyButton;
    private String username;

    public void setUsername(String username){
        this.username=username;
    }

    public void onApplyButtonEvent(){
        barChart.getData().clear();

        try {
            //Zum überprüfen ob bei beiden DatePickern ein Datum augewählt wurde
            startDatePicker.getValue().isBefore(endDatePicker.getValue());
        } catch (Exception e) {
            Alerts.noDateSelected();
            return;
        }
        //Zum überprüfen ob ein gültiger Zeitraum angegeben wurde
        if(endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            Alerts.endDateBeforeStartDate();
            return;
        }

        //Der Favoritenfilm wird angezeigt
        String favorite = HttpRequests.get(this.username,"/usersProfile/getFavoriteMovie").body();
        //Falls vorher kein Film als Favorit ausgewählt wurde
        if(favorite.isEmpty()) {
            Alerts.noFavoriteMovie();
            return;
        }
        filmNameLabel.setText(favorite);

        //DatePicker Daten werden zu einem String umgewandelt, nötig für HttpRequest
        String startDateString = startDatePicker.getValue().toString();
        String endDateString = endDatePicker.getValue().toString();

        //Statistikdaten zu Genre als String werden vom Server von der DB geschickt
        HttpResponse<String> genreString = HttpRequests.getStats("/usersProfile/getGenre", this.username, startDateString, endDateString);

        if(genreString.body().equals("{}")) {
            Alerts.noDataInThisRange();
            return;
        }

        //Aus dem String wird eine Hashmap gebaut, der String wird vom Server schon im Hashmap Format geschickt
        String substringGenre = genreString.body().substring(1,genreString.body().length()-1);
        substringGenre = substringGenre.replaceAll("\"","" );
        HashMap<String, Integer> genre = new HashMap<String, Integer>();
        String[] pairsGenre = substringGenre.split(",");
        for(int i=0; i<pairsGenre.length; i++) {
            String pair = pairsGenre[i];
            String[] keyValue = pair.split(":");
            genre.put(keyValue[0], Integer.valueOf(keyValue[1]));
        }

        //Statistikdaten zu Cast als String werden vom Server von der DB geschickt
        HttpResponse<String> castString = HttpRequests.getStats("/usersProfile/getCast", this.username, startDateString, endDateString);

        //Aus dem String wird eine Hashmap gebaut, der String wird vom Server schon im Hashmap Format geschickt
        String substringCast = castString.body().substring(1,castString.body().length()-1);
        substringCast = substringCast.replaceAll("\"","" );
        HashMap<String, Integer> cast = new HashMap<String, Integer>();
        String[] pairsCast = substringCast.split(",");
        for(int i=0; i<pairsCast.length; i++) {
            String pair = pairsCast[i];
            String[] keyValue = pair.split(":");
            cast.put(keyValue[0], Integer.valueOf(keyValue[1]));
        }

        //HashMaps von Genre und Cast werden nach Values in absteigender Reihenfolge sortiert
        genre = sortByValueDesc(genre);
        cast = sortByValueDesc(cast);

        //Balkendiagramm zu Genre wird erzeugt
        XYChart.Series genreSeries = new XYChart.Series();
        genreSeries.setName("Genre");
        for(String key : genre.keySet()) {
            if(genreSeries.getData().size()==5) {
                break;
            }
            genreSeries.getData().add(new XYChart.Data(key,genre.get(key)));
        }

        //Balkendiagramm zu Cast wird erzeugt
        XYChart.Series castSeries = new XYChart.Series();
        castSeries.setName("Actor");
        for(String key : cast.keySet()) {
            if(castSeries.getData().size()==5) {
                break;
            }
            castSeries.getData().add(new XYChart.Data(key,cast.get(key)));
        }

        //Balkendiagramm wird geladen
        barChart.getData().addAll(genreSeries, castSeries);

        //Die Gesamtlänge aller geschauten Filme im angegebenen Zeitraum wird vom Server geholt und das Gesamtzeitlabel wird auf diese Zahl geändert
        HttpResponse<String> filmlengthResponse = HttpRequests.getStats("/usersProfile/getFilmlengthStat", this.username, startDateString, endDateString);
        String filmlengthString = filmlengthResponse.body();
        watchedMinutesLabel.setText(filmlengthString);

        minutenLabel.setVisible(true);
        watchedMinutesLabel.setVisible(true);
        filmNameLabel.setVisible(true);
    }

    //Methode um Hashmap nach Values in absteigender Reihenfolge zu ordnen
    //BEMERKUNG: Diese Methode wurde nicht von Jan Sowa geschrieben, sondern ist von Stack Overflow!
    public HashMap<String, Integer> sortByValueDesc(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}

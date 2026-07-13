package com.sep.server.services;

import com.sep.server.model.RateMovie;
import com.sep.server.dbaccess.RateMovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class RateMovieService {

    private final RateMovieRepository rateMovieRepository;

    public RateMovieService(RateMovieRepository rateMovieRepository) {
        this.rateMovieRepository = rateMovieRepository;
    }

    //Rezension hinzufügen
    public ResponseEntity<String> createMovieRating(RateMovie ratemovie){

        if(!rateMovieRepository.existsByRatingname(ratemovie.getRatingname())){
            rateMovieRepository.save(ratemovie);
            return new ResponseEntity("created" + ratemovie.getRatingname(), HttpStatus.OK);
        }
        else{
            deleteMovieRating(ratemovie);
            rateMovieRepository.save(ratemovie);
            return new ResponseEntity("created" + ratemovie.getRatingname(), HttpStatus.OK);
        }
    }

    //gesehenen Film hinzufügen
    public ResponseEntity<String> addSeenMovie(RateMovie ratemovie){

        if(!rateMovieRepository.existsByRatingname(ratemovie.getRatingname())){
            rateMovieRepository.save(ratemovie);
            return new ResponseEntity("created" + ratemovie.getRatingname(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity("Already in Database",HttpStatus.IM_USED);
        }
    }

    //Rezension löschen
    public ResponseEntity<String> deleteMovieRating(RateMovie ratemovie){
        if(rateMovieRepository.existsByRatingname(ratemovie.getRatingname())) {
            rateMovieRepository.deleteById(ratemovie.getRatingname());
            return new ResponseEntity("deleted " + ratemovie.getRatingname(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(ratemovie.getRatingname()+" not found", HttpStatus.NO_CONTENT);
        }
    }

    //spezifische Rezension suchen
    public ResponseEntity<List<RateMovie>> findMovieRating(String name) {

        List<String> ratemovie = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from rate_movie where ratingname like '" + name + "'");
            ResultSet a = state.executeQuery();
            while (a.next()) {
                ratemovie.add(a.getString("username"));
                ratemovie.add(a.getString("movie_name"));
                ratemovie.add(a.getString("rating"));
                ratemovie.add(a.getString("rating_Caption"));
                ratemovie.add(a.getString("rating_Text"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(ratemovie, HttpStatus.OK);
    }

    //Rezensionen zu einem Film suchen
    public ResponseEntity<List<RateMovie>> getMovieratings(String name) {

        List<String> ratemovie = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from rate_movie where rating != 0 AND movie_name like '" + name + "'");
            ResultSet a = state.executeQuery();
            while (a.next()) {
                ratemovie.add(a.getString("username"));
                ratemovie.add(a.getString("rating_caption"));
                ratemovie.add(a.getString("rating_text"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(ratemovie, HttpStatus.OK);
    }

    //Anzahl gesehen
    public ResponseEntity<List<RateMovie>> movieSeen(String name) {

        List<String> ratemovie = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("SELECT COUNT(ratingname) AS anzahl FROM `rate_movie` where movie_name like'" + name + "'");
            ResultSet a = state.executeQuery();
            while (a.next()) {
                ratemovie.add(a.getString("anzahl"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(ratemovie, HttpStatus.OK);
    }

    //Reviews zählen
    public ResponseEntity<List<RateMovie>> countReviews(String name) {

        List<String> ratemovie = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("SELECT COUNT(ratingname) AS anzahl FROM `rate_movie` where rating != 0 AND movie_name like'" + name + "'");
            ResultSet a = state.executeQuery();
            while (a.next()) {
                ratemovie.add(a.getString("anzahl"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(ratemovie, HttpStatus.OK);
    }

    //(Admin) Statistik zu einem Film zurücksetzen (Global, Anzahl Reviews, Anzahl gesehen)
    public ResponseEntity<String> resetStatistic(RateMovie ratemovie) {

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {

            PreparedStatement state = con.prepareStatement("Delete From `rate_movie` Where movie_name='" + ratemovie.getMovieName() +"'");
            state.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("erfolgreich", HttpStatus.OK);
    }

    //vorhandene Bewertungen zu einem Film in der DB abrufen
    public ResponseEntity<List<RateMovie>> getGlobalRating(String name) {

        List<String> ratemovie = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from `rate_movie` where rating != 0 AND movie_name like '" + name + "'");
            ResultSet a = state.executeQuery();
            while (a.next()) {
                ratemovie.add(a.getString("rating"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(ratemovie, HttpStatus.OK);
    }


}

package com.sep.server.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sep.server.services.RateMovieService;
import com.sep.server.model.RateMovie;
import java.util.*;

@RestController
public class RateMovieRestController {

    private RateMovieService rateMovieService;


    public RateMovieRestController(RateMovieService rateMovieService) {

        this.rateMovieService = rateMovieService;
    }


    //Rezension hinzufügen
    @PostMapping (path= "ratemovie/add")
    public ResponseEntity<String> addMovieRating(@RequestBody RateMovie ratemovie){
        return rateMovieService.createMovieRating(ratemovie);
    }

    //Rezension löschen
    @PostMapping (path= "ratemovie/delete")
    public ResponseEntity<String> deleteMovieRating(@RequestBody RateMovie ratemovie){
        return rateMovieService.deleteMovieRating(ratemovie);
    }

    //spezifische Rezension suchen
    @GetMapping(path = "ratemovie/findrating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateMovie>> findMovieRating(@RequestParam(required = false, value = "search") String name){
        return rateMovieService.findMovieRating(name);
    }

    //Rezensionen zu einem Film suchen
    @GetMapping(path = "ratemovie/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateMovie>> getMovieratingsByMoviename(@RequestParam(required = false, value = "search") String name){
        return rateMovieService.getMovieratings(name);
    }

    //gesehenen Film hinzufügen
    @PostMapping (path= "ratemovie/addseenmovie")
    public ResponseEntity<String> addSeenMovie(@RequestBody RateMovie ratemovie){
        return rateMovieService.addSeenMovie(ratemovie);
    }

    //Anzahl gesehen
    @GetMapping(path = "ratemovie/movieseen", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateMovie>> movieSeen(@RequestParam(required = false, value = "search") String name){
        return rateMovieService.movieSeen(name);
    }

    //Reviews zählen
    @GetMapping(path = "ratemovie/countreviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateMovie>> countReviews(@RequestParam(required = false, value = "search") String name){
        return rateMovieService.countReviews(name);
    }

    //(Admin) Statistik zu einem Film zurücksetzen (Global, Anzahl Reviews, Anzahl gesehen)
    @PostMapping (path= "ratemovie/resetstatistic")
    public ResponseEntity<String> resetStatistic(@RequestBody RateMovie ratemovie){
        return rateMovieService.resetStatistic(ratemovie);
    }

    //vorhandene Bewertungen zu einem Film in der DB abrufen
    @GetMapping(path = "ratemovie/getglobal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateMovie>> getGlobalRating(@RequestParam(required = false, value = "search") String name){
        return rateMovieService.getGlobalRating(name);
    }

}

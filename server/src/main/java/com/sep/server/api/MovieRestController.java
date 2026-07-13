package com.sep.server.api;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sep.server.model.Movie;
import com.sep.server.services.MovieService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieRestController {
    private MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService=movieService;
    }


    //Film hinzufügen
    @PostMapping(path = "movie/add")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie){return movieService.createMovie(movie);}

    //Banner hinzufügen
    @PostMapping(path = "movie/setBanner")
    public ResponseEntity<String> bannerUpload(@RequestBody Movie movie){return movieService.createBanner(movie);}

    //Film löschen
    @PostMapping(path = "movie/delete")
    public ResponseEntity<String> deleteMovie(@RequestBody Movie movie){return movieService.deleteMovie(movie);}

    @PostMapping(path = "movie/addToWatchlist")
    public ResponseEntity<String> addToWatchlist(@RequestParam(required = false, value = "search") String username, @RequestBody String moviename){
        return movieService.addToWatchlist(username, moviename);
    }

    @PostMapping(path = "movie/addToWatchedlist")
    public ResponseEntity<String> addToWatchedlist(@RequestParam(required = false, value = "search") String username, @RequestBody String moviename){
        return movieService.addToWatchedlist(username, moviename);
    }

    @PostMapping(path = "movie/deleteFromWatchlist")
    public ResponseEntity<String> deleteFromWatchlist(@RequestParam(required = false, value = "search") String username, @RequestBody String moviename){
        return movieService.deleteFromWatchlist(username, moviename);
    }

    @PostMapping(path = "movie/deleteFromWatchedlist")
    public ResponseEntity<String> deleteFromWatchedlist(@RequestParam(required = false, value = "search") String username, @RequestBody String moviename){
        return movieService.deleteFromWatchedlist(username, moviename);
    }

    @PostMapping(path = "movie/setFilter")
    public ResponseEntity<String> setFilter(@RequestParam(required = false, value = "username") String username,
                                            @RequestParam(required = false, value = "filmlength") String filmlength,
                                            @RequestParam(required = false, value = "releaseYear") String releaseYear,
                                            @RequestParam(required = false, value = "regisseur") String regisseur,
                                            @RequestParam(required = false, value = "director") String director,
                                            @RequestParam(required = false, value = "cast") String cast,
                                            @RequestParam(required = false, value = "category") String category)
    {return movieService.setFilter(username, filmlength, releaseYear, regisseur, director, cast, category);}

    @GetMapping(path="movie/areFiltersEnabled")
    public ResponseEntity<Boolean> areFiltersEnabled(@RequestParam(required = false, value = "search") String searchVal) {
        return movieService.areFiltersEnabled(searchVal);
    }

    @GetMapping(path="movie/getFilters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getFilters(@RequestParam(required = false, value = "search") String searchVal) {
        return movieService.getFilters(searchVal);
    }

    @GetMapping(path = "movie/existsMovie")
    public ResponseEntity<String> findExistingMovie(@RequestParam(required = false, value = "search") String searchVal) {
        return movieService.existsMovie(searchVal);
    }

    @GetMapping(path = "movie/getBanner")
    public ResponseEntity<File> getBanner(@RequestParam(required = false, value = "search") String searchVal) {
        return movieService.getBannerOf(searchVal);
    }
    @GetMapping(path = "movie/isMovieInWatchlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isMovieInWatchlist(@RequestParam(required = false, value = "username") String username, @RequestParam(required = false, value="moviename") String moviename){
        return movieService.isMovieInWatchlist(username, moviename);
    }

    @GetMapping(path = "movie/isMovieInWatchedlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isMovieInWatchedlist(@RequestParam(required = false, value = "username") String username, @RequestParam(required = false, value="moviename") String moviename){
        return movieService.isMovieInWatchedlist(username, moviename);
    }

    //Alle Filmnamen mit einer Filterbedingung anfordern
    @GetMapping(path = "movie/requestSpecificNames", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> requestMovieSpecificMovieNames(@RequestParam(required = false, value = "search") String searchVal){
        return movieService.getSpecificMovieNames(searchVal);
    }

    @GetMapping(path = "movie/requestSpecificNamesWithFilters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> requestMovieSpecificMovieNamesWithFilters(@RequestParam(required = false, value = "moviename") String moviename,
                                                                                 @RequestParam(required = false, value = "filmlength") String filmlength,
                                                                                 @RequestParam(required = false, value = "releaseYear") String releaseYear,
                                                                                 @RequestParam(required = false, value = "regisseur") String regisseur,
                                                                                 @RequestParam(required = false, value = "director") String director,
                                                                                 @RequestParam(required = false, value = "cast") String cast,
                                                                                 @RequestParam(required = false, value = "category") String category){
        return movieService.getSpecificMovieNamesWithFilters(moviename, filmlength, releaseYear, regisseur, director, cast, category);
    }

    //Einen bestimmten Film anfordern
    @GetMapping(path = "movie/requestSpecificMovie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> requestMovieSpecificMovie(@RequestParam(required = false, value = "search") String searchVal){
        return movieService.getSpecificMovie(searchVal);
    }

    @GetMapping(path = "movie/getMovie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Movie> getMovie(@RequestParam(required = false, value = "search") String searchVal){
        return movieService.getMovieWithID(searchVal);
    }

    @GetMapping(path = "movie/banner", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findUsersPFP(@RequestParam(required = false, value = "search") String searchVal) {
        return movieService.showMovieCover(searchVal);
    }

    @PostMapping (path= "movie/setglobal")
    public ResponseEntity<String> setGlobalRating(@RequestBody Movie movie){
        return movieService.setGlobalRating(movie);
    }

    @PostMapping (path= "movie/resetglobal")
    public ResponseEntity<String> resetGlobalRating(@RequestBody Movie movie){
        return movieService.setGlobalRating(movie);
    }

    @GetMapping(path = "movie/getglobal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> getGlobal(@RequestParam(required = false, value = "search") String name){
        return movieService.getGlobal(name);
    }

    @GetMapping(path = "movie/bestMovies")
    public ResponseEntity<String> getBestMovies(@RequestParam(required = false,value = "search") String username) {
        return movieService.getBestMovieRecommendation(username);
    }
}

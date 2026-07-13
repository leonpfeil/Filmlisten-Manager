package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RateMovie {


    @Id
    @JsonProperty("ratingname")
    private String ratingname;

    @JsonProperty("username")
    private String username;

    @JsonProperty("movieName")
    private String movieName;

    @JsonProperty("movieSeen")
    private boolean movieSeen;

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("ratingCaption")
    private String ratingCaption;

    @JsonProperty("ratingText")
    private String ratingText;

    public String getRatingname() {
        return ratingname;
    }

    public void setRatingname(String ratingname) {
        this.ratingname = ratingname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public boolean isMovieSeen() {
        return movieSeen;
    }

    public void setMovieSeen(boolean movieSeen) {
        this.movieSeen = movieSeen;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingCaption() {
        return ratingCaption;
    }

    public void setRatingCaption(String ratingCaption) {
        this.ratingCaption = ratingCaption;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }
}

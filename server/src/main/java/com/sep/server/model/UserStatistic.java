package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserStatistic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @JsonProperty("username")
    private String username;

    @JsonProperty("movieName")
    private String movieName;

    @JsonProperty("movieLength")
    private Integer movieLength;

    @JsonProperty("cast")
    private String cast;

    @JsonProperty("applyDate")
    @Column(columnDefinition = "date")
    private Date date;

    @JsonProperty("genre")
    private String genre;

    public Integer getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(Integer movieLength) {
        this.movieLength = movieLength;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

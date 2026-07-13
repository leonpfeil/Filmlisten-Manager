package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class MovieInvitation {
    @Id
    @JsonProperty("moviename")
    private String movieName;
    @JsonProperty("time")
    private String time;
    @JsonProperty("text")
    private String text;
    @JsonProperty("date")
    private String date;
    @JsonProperty("sender")
    private String sender;
    @JsonProperty("done")
    private Boolean done;
    @JsonProperty("target")
    private String target;

    /*public MovieInvitation(String movieName, String time, String text, String date, String sender, Boolean done, String target) {
        this.movieName = movieName;
        this.time = time;
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.done = done;
        this.target = target;
    }*/

    public String getMovieName() {
        return movieName;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getTarget() {
        return target;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}

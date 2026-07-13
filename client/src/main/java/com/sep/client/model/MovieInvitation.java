package com.sep.client.model;

import java.sql.Date;

public class MovieInvitation {
    private String sender;
    private String target;
    private String movieName;
    private String date;
    private String time;
    private String message;

    public MovieInvitation(String sender, String target, String movieName, String date, String time, String message) {
        this.sender = sender;
        this.target = target;
        this.movieName = movieName;
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

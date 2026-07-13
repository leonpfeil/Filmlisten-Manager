package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.engine.internal.Cascade;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonProperty("reportID")
    Long reportID;

    @JsonProperty("username")
    String username;

    @JsonProperty("reportMessage")
    String reportMessage;

    boolean done;

    @JsonProperty("movieName")
    String movieName;

    public Long getReportID() {
        return reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    public boolean isDone() {return done;}

    public void setDone(boolean done) {this.done = done;}

    public String getMovieName() {return movieName;}

    public void setMovieName(String movieName) {this.movieName = movieName;}
}

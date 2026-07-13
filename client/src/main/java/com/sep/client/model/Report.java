package com.sep.client.model;

import javafx.beans.property.SimpleStringProperty;

public class Report {


    private String reportID;

    private String movieName;

    private String reportString;

    private String user;

    public Report(String reportID, String movieName, String reportString, String user) {
        this.reportID = reportID;
        this.movieName = movieName;
        this.reportString = reportString;
        this.user = user;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReportString() {
        return reportString;
    }

    public void setReportString(String reportString) {
        this.reportString = reportString;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

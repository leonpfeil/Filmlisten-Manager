package com.sep.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RateMovie {

    private String username;
    private String caption;
    private String text;

    public RateMovie(String username, String caption, String text) {
        this.username = username;
        this.caption = caption;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
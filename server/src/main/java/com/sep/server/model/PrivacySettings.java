package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PrivacySettings {
    //Wert auf 0 = öffentlich
    //Wert auf 1 = nur Freunde
    //Wert auf 2 = privat

    @Id
    @JsonProperty
    private String username;

    @JsonProperty
    private String friendsList;

    @JsonProperty
    private String watchedList;

    @JsonProperty
    private String watchlist;

    @JsonProperty
    private String reviews;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(String friendsList) {
        this.friendsList = friendsList;
    }

    public String getWatchedList() {
        return watchedList;
    }

    public void setWatchedList(String watchedList) {
        this.watchedList = watchedList;
    }

    public String getWatchList() {
        return watchlist;
    }

    public void setWatchList(String watchlist) {
        this.watchlist = watchlist;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}

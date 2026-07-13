package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class UserProfile  {
    @Id
    @JsonProperty("username")
    private String username;
    @JsonProperty("watchlist")
    @Column(columnDefinition = "Mediumtext")
    private String watchlist;
    @JsonProperty("watchedlist")
    @Column(columnDefinition = "Mediumtext")
    private String watchedlist;
    @JsonProperty("friendslist")
    @Column(columnDefinition = "Mediumtext")
    private String friendslist;
    @JsonProperty("favorite movie")
    private String favoriteMovie;
    @OneToOne
    @MapsId
    private User user;

    public String getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setFavoriteMovie(String favoriteMovie) {
        this.favoriteMovie = favoriteMovie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(String watchlist) {
        this.watchlist = watchlist;
    }

    public String getWatchedlist() {
        return watchedlist;
    }

    public void setWatchedlist(String watchedlist) {
        this.watchedlist = watchedlist;
    }

    public String getFriendslist() {
        return friendslist;
    }

    public void setFriendslist(String friendslist) {
        this.friendslist = friendslist;
    }
}

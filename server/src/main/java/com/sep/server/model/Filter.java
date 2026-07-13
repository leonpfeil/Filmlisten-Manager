package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Filter {

    @Id
    @JsonProperty("username")
    private String username;

    @JsonProperty("filmlength")
    private String filmlengthFilter;

    @JsonProperty("releaseYear")
    private String releaseYearFilter;

    @JsonProperty("regisseur")
    private String regisseurFilter;

    @JsonProperty("director")
    private String directorFilter;

    @JsonProperty("cast")
    private String castFilter;

    @JsonProperty("category")
    private String categoryFilter;

    @OneToOne
    @MapsId
    private UserProfile userProfile;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilmlengthFilter() {
        return filmlengthFilter;
    }

    public void setFilmlengthFilter(String filmlengthFilter) {
        this.filmlengthFilter = filmlengthFilter;
    }

    public String getReleaseYearFilter() {
        return releaseYearFilter;
    }

    public void setReleaseYearFilter(String releaseYearFilter) {
        this.releaseYearFilter = releaseYearFilter;
    }

    public String getRegisseurFilter() {
        return regisseurFilter;
    }

    public void setRegisseurFilter(String regisseurFilter) {
        this.regisseurFilter = regisseurFilter;
    }

    public String getDirectorFilter() {
        return directorFilter;
    }

    public void setDirectorFilter(String directorFilter) {
        this.directorFilter = directorFilter;
    }

    public String getCastFilter() {
        return castFilter;
    }

    public void setCastFilter(String castFilter) {
        this.castFilter = castFilter;
    }

    public String getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(String categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}

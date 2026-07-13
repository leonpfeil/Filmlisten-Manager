package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.awt.image.BufferedImage;
import java.util.List;


@Entity
public class Movie {
    @Id
    @JsonProperty("movieName")
    private String movieName;
    @JsonProperty("category")
    private String category;
    @JsonProperty("length")
    private int length;
    @JsonProperty("releaseYear")
    private int releaseYear;
    @JsonProperty("author")
    private String author;
    @JsonProperty("director")
    private String director;
    @JsonProperty("cast")
    private String cast;
    @JsonProperty("bannerPath")
    private String bannerPath;
    @JsonProperty("globalrating")
    private float globalrating;



//Getter und Setter

    public float getGlobalrating() {
        return globalrating;
    }

    public void setGlobalrating(float globalrating) {
        this.globalrating = globalrating;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {return cast;}

    public void setCast(String cast) {this.cast = cast;}

    public String getBannerPath() {return bannerPath;}

    public void setBannerPath(String bannerPath) {this.bannerPath = bannerPath;}
}

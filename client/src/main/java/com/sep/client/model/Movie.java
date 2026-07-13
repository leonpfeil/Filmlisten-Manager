package com.sep.client.model;

import java.util.Date;


public class Movie {

    public Movie(String movieName,
                 String category,
                 int length,
                 Date release,
                 String author,
                 String director,
                 String cast) {

        this.movieName = movieName;
        this.category = category;
        this.length = length;
        this.release = release;
        this.author = author;
        this.director = director;
        this.cast = cast;

    }

    private String movieName;
    private String category;
    private int length;
    private Date release;
    private String author;
    private String director;
    private String cast;



    //Getter und Setter

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

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
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

}

package com.example.cinefast3;

import java.io.Serializable;

public class Movie implements Serializable {
    private String name;
    private String genre;
    private int posterResource;
    private String trailerUrl;
    private boolean isNowShowing;

    public Movie(String name, String genre, int posterResource, String trailerUrl, boolean isNowShowing) {
        this.name = name;
        this.genre = genre;
        this.posterResource = posterResource;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public int getPosterResource() { return posterResource; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isNowShowing() { return isNowShowing; }
}

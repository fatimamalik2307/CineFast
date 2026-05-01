package com.example.cinefast3;

import java.io.Serializable;

public class Movie implements Serializable {
    private String name;
    private String genre;
    private String posterName;
    private int posterResource;
    private String trailerUrl;
    private boolean isNowShowing;
    private String date; // Added for persistence
    private String time; // Added for persistence

    public Movie(String name, String genre, String posterName, String trailerUrl, boolean isNowShowing, String date, String time) {
        this.name = name;
        this.genre = genre;
        this.posterName = posterName;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
        this.date = date;
        this.time = time;
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public String getPosterName() { return posterName; }
    public int getPosterResource() { return posterResource; }
    public void setPosterResource(int posterResource) { this.posterResource = posterResource; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isNowShowing() { return isNowShowing; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}

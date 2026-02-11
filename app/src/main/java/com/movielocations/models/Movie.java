package com.movielocations.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String type; // "movie" or "tvshow"
    private String language;
    private String description;
    private String posterUrl;
    private int year;

    public Movie(String title, String type, String language, String description, String posterUrl, int year) {
        this.title = title;
        this.type = type;
        this.language = language;
        this.description = description;
        this.posterUrl = posterUrl;
        this.year = year;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}

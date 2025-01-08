package com.example.model;

public class Movie {

    private String title;
    private String genre;
    private String poster;
    private String summary;

    public Movie(String title, String genre, String poster, String summary) {
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

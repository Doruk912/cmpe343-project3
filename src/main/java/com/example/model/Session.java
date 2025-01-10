package com.example.model;
import java.time.LocalDate;
import java.time.LocalTime;


public class Session {
    private int id;
    private int movieId;
    private LocalDate date;
    private LocalTime time;
    private String location;

    public Session(int id, int movieId, LocalDate date, LocalTime time, String location) {
        this.id = id;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
        this.location = location;
    }
    public int getId() {
        return id; }
    public void setId(int id) {
        this.id = id; }
    public int getMovieId() {
        return movieId; }
    public void setMovieId(int movieId) {
        this.movieId = movieId; }
    public LocalDate getDate() {
        return date; }
    public void setDate(LocalDate date) {
        this.date = date; }
    public LocalTime getTime() {
        return time; }
    public void setTime(LocalTime time) {
        this.time = time; }
    public String getLocation() {
        return location; }
    public void setLocation(String location) {
        this.location = location; }

}


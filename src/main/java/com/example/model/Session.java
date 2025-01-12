package com.example.model;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a movie session in the cinema.
 *
 * This class stores information about a specific movie session, including the session's ID,
 * the movie associated with the session, the date and time of the session, and its location.
 */
public class Session {
    private int id;
    private int movieId;
    private LocalDate date;
    private LocalTime time;
    private String location;

    /**
     * Constructs a Session with the specified details.
     *
     * @param id        the unique ID of the session
     * @param movieId   the ID of the movie associated with this session
     * @param date      the date of the session
     * @param time      the time of the session
     * @param location  the location of the session
     */
    public Session(int id, int movieId, LocalDate date, LocalTime time, String location) {
        this.id = id;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
        this.location = location;
    }
    /**
     * Gets the unique ID of the session.
     *
     * @return the unique ID of the session
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the session.
     *
     * @param id the unique ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the movie associated with this session.
     *
     * @return the movie ID of the session
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Sets the ID of the movie associated with this session.
     *
     * @param movieId the movie ID to set
     */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    /**
     * Gets the date of the session.
     *
     * @return the date of the session
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the session.
     *
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the time of the session.
     *
     * @return the time of the session
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time of the session.
     *
     * @param time the time to set
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Gets the location of the session.
     *
     * @return the location of the session
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the session.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
package com.example.model;

/**
 * Represents a movie in the cinema system.
 *
 * This class stores information about a movie, including its title, genre, summary, poster image, and a unique ID.
 */
public class Movie {

    private int id;
    private String title;
    private String genre;
    private String poster;
    private String summary;

    /**
     * Constructs a Movie with the specified title, genre, poster, and summary.
     *
     * @param title   the title of the movie
     * @param genre   the genre of the movie
     * @param poster  the URL or path to the movie poster image
     * @param summary a short summary or description of the movie
     */
    public Movie(String title, String genre, String poster, String summary) {
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.poster = poster;
    }

    /**
     * Constructs a Movie with the specified ID, title, genre, poster, and summary.
     *
     * @param id      the unique ID of the movie
     * @param title   the title of the movie
     * @param genre   the genre of the movie
     * @param poster  the URL or path to the movie poster image
     * @param summary a short summary or description of the movie
     */
    public Movie(int id, String title, String genre, String poster, String summary) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.poster = poster;
    }

    /**
     * Gets the title of the movie.
     *
     * @return the title of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the genre of the movie.
     *
     * @return the genre of the movie
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the movie.
     *
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the summary of the movie.
     *
     * @return the summary of the movie
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary of the movie.
     *
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets the poster of the movie.
     *
     * @return the poster image URL or path
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Sets the poster of the movie.
     *
     * @param poster the poster image URL or path to set
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Gets the unique ID of the movie.
     *
     * @return the unique ID of the movie
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the movie.
     *
     * @param id the unique ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the Movie object.
     *
     * @return a string representation of the Movie object
     */
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", poster='" + poster + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}

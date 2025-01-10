package com.example.controller;

import com.example.model.Movie;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MovieListCell extends ListCell<Movie> {
    private final HBox content;
    private final ImageView posterImageView;
    private final VBox details;
    private final Text title;
    private final Text genreLabel;
    private final Text genre;
    private final Text summary;

    public MovieListCell() {
        super();
        posterImageView = new ImageView();
        posterImageView.setFitHeight(100);
        posterImageView.setFitWidth(70);
        title = new Text();
        title.setStyle("-fx-font-weight: bold;");
        genreLabel = new Text("Genre: ");
        genre = new Text();
        summary = new Text();
        summary.setWrappingWidth(500);
        details = new VBox(title, new HBox(genreLabel, genre, new Text("\n")), summary);
        content = new HBox(posterImageView, details);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);
        if (empty || movie == null) {
            setGraphic(null);
        } else {
            Image posterImage = new Image(movie.getPoster(), true);
            posterImageView.setImage(posterImage);
            title.setText(movie.getTitle());
            genre.setText(movie.getGenre());
            summary.setText(movie.getSummary());
            setGraphic(content);
        }
    }
}
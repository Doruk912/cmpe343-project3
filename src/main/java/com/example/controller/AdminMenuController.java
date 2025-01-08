package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;

import java.util.List;

public class AdminMenuController {

    @FXML
    private TableView<Movie> movieTable;

    @FXML
    private TableColumn<Movie, String> titleColumn;

    @FXML
    private TableColumn<Movie, String> genreColumn;

    @FXML
    private TableColumn<Movie, String> summaryColumn;

    @FXML
    private TableColumn<Movie, String> posterColumn;

    @FXML
    private Label selectedTitle;

    @FXML
    private Label selectedGenre;

    @FXML
    private Label selectedSummary;

    @FXML
    private ImageView selectedPoster;

    private final ObservableList<Movie> movieList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));

        DatabaseConnection db = new DatabaseConnection();
        List<Movie> movies = db.getAllMovies();
        movieList.setAll(movies);
        movieTable.setItems(movieList);

        if (!movieList.isEmpty()) {
            movieTable.getSelectionModel().selectFirst();
            displayMovieDetails(movieList.getFirst());
        }

        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> displayMovieDetails(newValue));
    }

    @FXML
    private void onAddMovie() {
        Dialog<Movie> dialog = new Dialog<>();
        dialog.setTitle("Add New Movie");
        dialog.setHeaderText("Enter movie details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField();
        TextField genreField = new TextField();
        TextField posterField = new TextField();
        TextArea summaryArea = new TextArea();
        summaryArea.setWrapText(true);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(new Label("Poster URL:"), 0, 2);
        grid.add(posterField, 1, 2);
        grid.add(new Label("Summary:"), 0, 3);
        grid.add(summaryArea, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String title = titleField.getText().trim();
                String genre = genreField.getText().trim();
                String poster = posterField.getText().trim();
                String summary = summaryArea.getText().trim();

                if (!title.isEmpty() && !genre.isEmpty() && !poster.isEmpty() && !summary.isEmpty()) {
                    if (!isValidImage(poster)) {
                        showAlert("Image Not Found", "The image at the provided URL could not be loaded.");
                        return null;
                    }
                    return new Movie(title, genre, poster, summary);
                } else {
                    showAlert("Invalid Input", "All fields must be filled.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(movie -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.addMovie(movie)) {
                movieList.add(movie);
                showInfo("Success", "Movie added successfully!");
            } else {
                showAlert("Error", "Failed to add the movie.");
            }
        });
    }

    private void displayMovieDetails(Movie movie) {
        if (movie != null) {
            selectedTitle.setText("Title: " + movie.getTitle());
            selectedGenre.setText("Genre: " + movie.getGenre());
            selectedSummary.setText("Summary: " + movie.getSummary());
            if(isValidImage(movie.getPoster())){
                selectedPoster.setImage(new Image(movie.getPoster()));
            } else {
                selectedPoster.setImage(new Image("https://fakeimg.pl/500x740?text=No+Poster&font=bebas"));
            }
        } else {
            clearMovieDetails();
        }
    }

    private void clearMovieDetails() {
        selectedTitle.setText("Title: ");
        selectedGenre.setText("Genre: ");
        selectedSummary.setText("Summary: ");
        selectedPoster.setImage(null);
    }

    public void onUpdateMovie(ActionEvent actionEvent) {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert("No Movie Selected", "Please select a movie to update.");
            return;
        }

        Dialog<Movie> dialog = new Dialog<>();
        dialog.setTitle("Update Movie");
        dialog.setHeaderText("Update movie details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField(selectedMovie.getTitle());
        TextField genreField = new TextField(selectedMovie.getGenre());
        TextField posterField = new TextField(selectedMovie.getPoster());
        TextArea summaryArea = new TextArea(selectedMovie.getSummary());
        summaryArea.setWrapText(true);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(new Label("Poster URL:"), 0, 2);
        grid.add(posterField, 1, 2);
        grid.add(new Label("Summary:"), 0, 3);
        grid.add(summaryArea, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String title = titleField.getText().trim();
                String genre = genreField.getText().trim();
                String poster = posterField.getText().trim();
                String summary = summaryArea.getText().trim();

                if (!title.isEmpty() && !genre.isEmpty() && !poster.isEmpty() && !summary.isEmpty()) {
                    if (!isValidImage(poster)) {
                        showAlert("Image Not Found", "The image at the provided URL could not be loaded.");
                        return null;
                    }
                    return new Movie(title, genre, poster, summary);
                } else {
                    showAlert("Invalid Input", "All fields must be filled.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(movie -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.updateMovie(selectedMovie.getTitle(), movie)) {
                movieList.set(movieList.indexOf(selectedMovie), movie);
                showInfo("Success", "Movie updated successfully!");
            } else {
                showAlert("Error", "Failed to update the movie.");
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidImage(String url) {
        try {
            Image image = new Image(url, 1, 1, true, true);
            return !image.isError();
        } catch (Exception e) {
            return false;
        }
    }
}

package com.example.controller;

import com.example.MainApplication;
import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class CashierMenuController {
    public Label usernameLabel;
    public ListView<Movie> movieListView;
    public ComboBox<String> dateComboBox;
    public ComboBox<String> sessionComboBox;
    public TextField searchTextField;
    public ComboBox<String> genreComboBox;

    private ObservableList<Movie> movieList;

    @FXML
    public void initialize() {
        loadMovies();
        movieListView.setCellFactory(param -> new MovieListCell());
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> filterMoviesByTitle(newValue));
        genreComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterMovies());
        loadGenres();
    }

    private void loadMovies() {
        DatabaseConnection db = new DatabaseConnection();
        List<Movie> movies = db.getAllMovies();
        movieList = FXCollections.observableArrayList(movies);
        movieListView.getItems().setAll(movies);
    }

    private void loadGenres() {
        DatabaseConnection db = new DatabaseConnection();
        List<String> genres = db.getAllGenres();
        genres.add(0, "All");
        genreComboBox.setItems(FXCollections.observableArrayList(genres));
        genreComboBox.setValue("All");
    }

    private void filterMoviesByTitle(String title) {
        List<Movie> filteredMovies = movieList.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        movieListView.setItems(FXCollections.observableArrayList(filteredMovies));
    }

    private void filterMovies() {
        String title = searchTextField.getText().toLowerCase();
        String genre = genreComboBox.getValue();
        List<Movie> filteredMovies = movieList.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title))
                .filter(movie -> genre.equals("All") || movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
        movieListView.setItems(FXCollections.observableArrayList(filteredMovies));
    }

    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void onContinue(ActionEvent actionEvent) {
    }
}

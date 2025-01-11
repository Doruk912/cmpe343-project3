package com.example.controller;

import com.example.MainApplication;
import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class CashierMenuController {
    public Label usernameLabel;
    public ListView<Movie> movieListView;
    public DatePicker datePicker;
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
        configureDatePicker();

        movieListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadAvailableSessions());
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> loadAvailableSessions());
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

    private void configureDatePicker() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });

        datePicker.setPromptText("dd/MM/yyyy");
    }

    private void loadAvailableSessions() {
        Movie selectedMovie = movieListView.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedMovie != null && selectedDate != null) {
            DatabaseConnection db = new DatabaseConnection();
            List<Session> sessions = db.getSessionsForMovie(selectedMovie.getId()).stream()
                    .filter(session -> session.getDate().equals(selectedDate))
                    .toList();

            if (sessions.isEmpty()) {
                sessionComboBox.setPromptText("No sessions");
                sessionComboBox.setItems(FXCollections.observableArrayList());
            } else {
                List<String> sessionDetails = sessions.stream()
                        .map(session -> session.getTime().toString() + " - " + session.getLocation())
                        .collect(Collectors.toList());
                sessionComboBox.setPromptText("Select a session");
                sessionComboBox.setItems(FXCollections.observableArrayList(sessionDetails));
            }
        } else {
            sessionComboBox.setPromptText("");
            sessionComboBox.getItems().clear();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void onContinue(ActionEvent actionEvent) {
        Movie selectedMovie = movieListView.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();
        String selectedSession = sessionComboBox.getSelectionModel().getSelectedItem();

        if (selectedMovie == null) {
            showAlert("Please select a movie.");
            return;
        }

        if (selectedDate == null) {
            showAlert("Please select a date.");
            return;
        }

        if (selectedSession == null) {
            showAlert("Please select a session.");
            return;
        }

        MainApplication app = new MainApplication();
        app.showTicketSelectionScreen((Stage) usernameLabel.getScene().getWindow(), selectedMovie, selectedDate, selectedSession, usernameLabel.getText());

    }
}

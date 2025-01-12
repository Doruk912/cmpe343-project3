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

/**
 * Controller class for managing the cashier menu, including movie selection, date, and session handling.
 * This class allows the cashier to filter movies, select a date, choose a session, and proceed to ticket selection.
 */
public class CashierMenuController {
    public Label usernameLabel;
    public ListView<Movie> movieListView;
    public DatePicker datePicker;
    public ComboBox<String> sessionComboBox;
    public TextField searchTextField;
    public ComboBox<String> genreComboBox;

    private ObservableList<Movie> movieList;

    /**
     * Initializes the controller by loading movies, configuring filters, and setting up UI components.
     * This method is automatically called after the FXML is loaded.
     */
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

    /**
     * Loads the list of movies from the database and populates the movie list view.
     */
    private void loadMovies() {
        DatabaseConnection db = new DatabaseConnection();
        List<Movie> movies = db.getAllMovies();
        movieList = FXCollections.observableArrayList(movies);
        movieListView.getItems().setAll(movies);
    }

    /**
     * Loads all available genres from the database and populates the genre combo box.
     */
    private void loadGenres() {
        DatabaseConnection db = new DatabaseConnection();
        List<String> genres = db.getAllGenres();
        genres.add(0, "All");
        genreComboBox.setItems(FXCollections.observableArrayList(genres));
        genreComboBox.setValue("All");
    }

    /**
     * Filters the movie list based on the search query entered in the title search field.
     * 
     * @param title the title query used for filtering.
     */
    private void filterMoviesByTitle(String title) {
        List<Movie> filteredMovies = movieList.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        movieListView.setItems(FXCollections.observableArrayList(filteredMovies));
    }

    /**
     * Filters the movie list based on both the search query and the selected genre.
     */
    private void filterMovies() {
        String title = searchTextField.getText().toLowerCase();
        String genre = genreComboBox.getValue();
        List<Movie> filteredMovies = movieList.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title))
                .filter(movie -> genre.equals("All") || movie.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
        movieListView.setItems(FXCollections.observableArrayList(filteredMovies));
    }

    /**
     * Configures the date picker to use a custom date format and handle conversion between String and LocalDate.
     */
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

    /**
     * Loads available sessions based on the selected movie and date, and updates the session combo box.
     */
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

    /**
     * Displays a warning alert with the provided message.
     *
     * @param message the message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the logout action and navigates back to the login screen.
     * 
     * @param actionEvent the action event triggered by the logout button.
     */
    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }

    /**
     * Sets the username label to the provided username.
     *
     * @param username the username to set in the label.
     */
    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    /**
     * Continues to the ticket selection screen after verifying that the movie, date, and session are selected.
     * If any of these are missing, a warning alert is shown.
     * 
     * @param actionEvent the action event triggered by the continue button.
     */
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

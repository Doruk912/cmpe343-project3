package com.example.controller;

import com.example.MainApplication;
import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class CashierMenuController {
    public Label usernameLabel;
    public ListView<Movie> movieListView;
    public ComboBox<String> dateComboBox;
    public ComboBox<String> sessionComboBox;
    public TextField searchTextField;
    public ComboBox genreComboBox;

    @FXML
    public void initialize() {
        loadMovies();
        movieListView.setCellFactory(param -> new MovieListCell());
    }

    private void loadMovies() {
        DatabaseConnection db = new DatabaseConnection();
        List<Movie> movies = db.getAllMovies();
        movieListView.getItems().setAll(movies);
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

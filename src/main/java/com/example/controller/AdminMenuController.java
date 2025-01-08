package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private TextField titleField;

    @FXML
    private TextField posterField;

    @FXML
    private TextField genreField;

    @FXML
    private TextArea summaryArea;

    @FXML
    private Button addMovieButton;

    @FXML
    private Button updateMovieButton;

    @FXML
    private Button saveChangesButton;

    private final ObservableList<Movie> movieList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        summaryColumn.setCellValueFactory(new PropertyValueFactory<>("summary"));
        posterColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(item));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);
                }
            }
        });

        movieTable.setItems(movieList);
        DatabaseConnection db = new DatabaseConnection();
        List<Movie> movies = db.getAllMovies();
        movieList.setAll(movies);

        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateFields(newValue));
    }

    @FXML
    private void onAddMovie() {

    }

    @FXML
    private void onUpdateMovie() {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            selectedMovie.setTitle(titleField.getText());
            selectedMovie.setGenre(genreField.getText());
            selectedMovie.setSummary(summaryArea.getText());
            selectedMovie.setPoster(posterField.getText());
            movieTable.refresh();
            clearFields();
        } else {
            showAlert("No Movie Selected", "Please select a movie to update.");
        }
    }

    @FXML
    private void onSaveChanges() {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            selectedMovie.setTitle(titleField.getText());
            selectedMovie.setGenre(genreField.getText());
            selectedMovie.setSummary(summaryArea.getText());
            selectedMovie.setPoster(posterField.getText());
            movieTable.refresh();
        } else {
            showAlert("No Movie Selected", "Please select a movie to save changes.");
        }
    }

    private void populateFields(Movie movie) {
        if (movie != null) {
            titleField.setText(movie.getTitle());
            genreField.setText(movie.getGenre());
            summaryArea.setText(movie.getSummary());
            posterField.setText(movie.getPoster());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        titleField.clear();
        genreField.clear();
        summaryArea.clear();
        posterField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
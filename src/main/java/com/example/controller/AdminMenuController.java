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
        // Create the dialog
        Dialog<Movie> dialog = new Dialog<>();
        dialog.setTitle("Add New Movie");
        dialog.setHeaderText("Enter movie details:");

        // Create labels and fields for input
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();

        Label summaryLabel = new Label("Summary:");
        TextArea summaryArea = new TextArea();
        summaryArea.setWrapText(true);

        // Arrange fields in a grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(genreLabel, 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(summaryLabel, 0, 2);
        grid.add(summaryArea, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Add buttons
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Convert result to a Movie object
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText().trim();
                String genre = genreField.getText().trim();
                String summary = summaryArea.getText().trim();

                if (!title.isEmpty() && !genre.isEmpty() && !summary.isEmpty()) {
                    return new Movie(title, genre, summary, null); // No poster for now
                } else {
                    showAlert("Invalid Input", "All fields must be filled.");
                    return null;
                }
            }
            return null;
        });

        // Show dialog and get result
        dialog.showAndWait().ifPresent(movie -> {
            // Add movie to database and update table
            DatabaseConnection db = new DatabaseConnection();
            if (db.addMovie(movie)) {
                movieList.add(movie); // Update UI
                showInfo("Success", "Movie added successfully!");
            } else {
                showAlert("Error", "Failed to add the movie. Please try again.");
            }
        });
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

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
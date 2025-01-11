package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleController {

    @FXML
    private TableView<Session> scheduleTable;

    @FXML
    private TableColumn<Session, Integer> idColumn;

    @FXML
    private TableColumn<Session, String> locationColumn;

    @FXML
    private TableColumn<Session, Date> dateColumn;

    @FXML
    private TableColumn<Session, Time> timeColumn;

    private final ObservableList<Session> sessionList = FXCollections.observableArrayList();

    @FXML
    private Label scheduleLabel;

    private Movie selectedMovie;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    public void handleAddSession(ActionEvent actionEvent) {
        Dialog<Session> dialog = new Dialog<>();
        dialog.setTitle("Add New Session");
        dialog.setHeaderText("Enter session details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        DatePicker datePicker = new DatePicker();
        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.getItems().addAll("10:00", "13:00", "16:00", "19:00", "22:00");
        ComboBox<String> locationComboBox = new ComboBox<>();
        locationComboBox.getItems().addAll("Hall 1", "Hall 2");

        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Time:"), 0, 1);
        grid.add(timeComboBox, 1, 1);
        grid.add(new Label("Location:"), 0, 2);
        grid.add(locationComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    LocalDate date = datePicker.getValue();
                    String timeString = timeComboBox.getValue();
                    String location = locationComboBox.getValue();

                    if (date != null && timeString != null && location != null) {
                        LocalTime time = LocalTime.parse(timeString);
                        return new Session(0, selectedMovie.getId(), date, time, location);
                    } else {
                        showAlert("Invalid Input", "All fields must be filled with valid values.");
                    }
                } catch (Exception e) {
                    showAlert("Invalid Input", "Please enter valid date and time.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(session -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.addSession(session)) {
                loadScheduleForMovie();
                showInfo("Success", "Session added successfully!");
            } else {
                showAlert("Error", "Failed to add the session.");
            }
        });
    }

    public void handleEditSession(ActionEvent actionEvent) {
        Session selectedSession = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            showAlert("No Selection", "No session selected. Please select a session to edit.");
            return;
        }

        Dialog<Session> dialog = new Dialog<>();
        dialog.setTitle("Edit Session");
        dialog.setHeaderText("Edit session details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        DatePicker datePicker = new DatePicker(selectedSession.getDate());
        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.getItems().addAll("10:00", "13:00", "16:00", "19:00", "22:00");
        timeComboBox.setValue(selectedSession.getTime().toString());
        ComboBox<String> locationComboBox = new ComboBox<>();
        locationComboBox.getItems().addAll("Hall 1", "Hall 2");
        locationComboBox.setValue(selectedSession.getLocation());

        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Time:"), 0, 1);
        grid.add(timeComboBox, 1, 1);
        grid.add(new Label("Location:"), 0, 2);
        grid.add(locationComboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    LocalDate date = datePicker.getValue();
                    String timeString = timeComboBox.getValue();
                    String location = locationComboBox.getValue();

                    if (date != null && timeString != null && location != null) {
                        LocalTime time = LocalTime.parse(timeString);
                        selectedSession.setDate(date);
                        selectedSession.setTime(time);
                        selectedSession.setLocation(location);
                        return selectedSession;
                    } else {
                        showAlert("Invalid Input", "All fields must be filled with valid values.");
                    }
                } catch (Exception e) {
                    showAlert("Invalid Input", "Please enter valid date and time.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(session -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.updateSession(session)) {
                loadScheduleForMovie();
                showInfo("Success", "Session updated successfully!");
            } else {
                showAlert("Error", "Failed to update the session.");
            }
        });
    }

    public void handleDeleteSession(ActionEvent actionEvent) {
        Session selectedSession = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            showAlert("No Selection", "No session selected. Please select a session to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Session");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected session?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DatabaseConnection db = new DatabaseConnection();
                if (db.removeSession(selectedSession)) {
                    loadScheduleForMovie();
                    showInfo("Success", "Session deleted successfully!");
                } else {
                    showAlert("Error", "Failed to delete the session.");
                }
            }
        });
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) scheduleTable.getScene().getWindow();
        stage.close();
    }

    public void setSelectedMovie(Movie movie) {
        this.selectedMovie = movie;
        scheduleLabel.setText("Schedule for " + movie.getTitle());
        loadScheduleForMovie();
    }

    private void loadScheduleForMovie() {
        if (selectedMovie != null) {
            DatabaseConnection db = new DatabaseConnection();
            List<Session> sessions = db.getSessionsForMovie(selectedMovie.getId());
            scheduleTable.setItems(FXCollections.observableArrayList(sessions));
        }
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

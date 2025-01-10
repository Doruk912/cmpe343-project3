package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ScheduleController {

    @FXML
    public TableView<Session> scheduleTable;

    @FXML
    public TableColumn<Session, Integer> idColumn;

    @FXML
    public TableColumn<Session, String> locationColumn;

    @FXML
    public TableColumn<Session, Date> dateColumn;

    @FXML
    public TableColumn<Session, Time> timeColumn;

    private final ObservableList<Session> sessionList = FXCollections.observableArrayList();

    private Movie selectedMovie;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        DatabaseConnection db = new DatabaseConnection();
        //sessionList.setAll(db.getAllSessions());
    }

    public void handleAddSession(ActionEvent actionEvent) {
    }

    public void handleEditSession(ActionEvent actionEvent) {
    }

    public void handleDeleteSession(ActionEvent actionEvent) {
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) scheduleTable.getScene().getWindow();
        stage.close();
    }

    public void setSelectedMovie(Movie movie) {
        this.selectedMovie = movie;
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

package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminMenuController {

    @FXML
    private Button addMovieButton;

    @FXML
    private Button updateMovieButton;

    @FXML
    private Button createScheduleButton;

    @FXML
    private Button processCancellationsButton;

    @FXML
    private void handleAddMovie() {
        // TODO: Navigate to the Add Movie screen or display the Add Movie form
        System.out.println("Add Movie Button Clicked");
    }

    @FXML
    private void handleUpdateMovie() {
        // TODO: Navigate to the Update Movie screen or display the Update Movie form
        System.out.println("Update Movie Button Clicked");
    }

    @FXML
    private void handleCreateSchedule() {
        // TODO: Navigate to the Create Schedule screen or display the Create Schedule form
        System.out.println("Create Schedule Button Clicked");
    }

    @FXML
    private void handleProcessCancellations() {
        // TODO: Navigate to the Process Cancellations screen or handle cancellations
        System.out.println("Process Cancellations Button Clicked");
    }
}
package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionController {

    @FXML
    public ImageView hallImageView;

    @FXML
    private Label availableSeatsLabel;

    @FXML
    private Label usernameLabel;

    private Movie movie;
    private LocalDate date;
    private String session;
    private int regularTickets;
    private int discountedTickets;
    private String hall;

    @FXML
    public void initialize() {

    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSession(String session) {
        this.session = session;
        extractHallFromSession();
        loadHallImage();
        loadAvailableSeats();
    }

    public void setRegularTickets(int regularTickets) {
        this.regularTickets = regularTickets;
    }

    public void setDiscountedTickets(int discountedTickets) {
        this.discountedTickets = discountedTickets;
    }

    public void setUsername(String username) {
        this.usernameLabel.setText(username);
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    private void extractHallFromSession() {
        if (session != null && session.contains(" - ")) {
            String[] parts = session.split(" - ");
            if (parts.length == 2) {
                this.hall = parts[1].trim();
            }
        }
    }

    private void loadHallImage() {
        System.out.println(session);
        if (hall.equals("Hall 1")) {
            hallImageView.setImage(new Image("/com/example/images/hall1.png"));
        } else if (hall.equals("Hall 2")) {
            hallImageView.setImage(new Image("/com/example/images/hall2.png"));
        }
    }

    private void loadAvailableSeats() {
        List<Integer> availableSeats = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT seat_number FROM seats WHERE session_id = (SELECT id FROM sessions WHERE movie_id = ? AND date = ? AND location = ?) AND is_taken = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, movie.getId());
            statement.setDate(2, java.sql.Date.valueOf(date));
            statement.setString(3, hall);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                availableSeats.add(resultSet.getInt("seat_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        availableSeatsLabel.setText("Available Seats: " + availableSeats.toString());
    }

    public void onBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/TicketSelection.fxml"));
            Parent root = loader.load();

            TicketSelectionController controller = loader.getController();
            controller.setUsername(usernameLabel.getText());

            Stage stage = (Stage) availableSeatsLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onConfirm(ActionEvent actionEvent) {
        // Handle seat confirmation logic
        // For example, save selected seats to the database or pass to the next screen
    }
}

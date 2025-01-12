/**
 * The SeatSelectionController class manages the seat selection functionality
 * for a movie ticket booking system. It handles user interactions for selecting
 * seats, displays available seats, and navigates between scenes.
 */
package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionController {

    /**
     * ImageView displaying the hall layout.
     */
    @FXML
    public ImageView hallImageView;

    /**
     * Label showing the number of available seats.
     */
    @FXML
    private Label availableSeatsLabel;

    /**
     * VBox container for displaying seat input fields.
     */
    @FXML
    private VBox seatInputVBox;

    /**
     * Label displaying the username of the logged-in user.
     */
    @FXML
    private Label usernameLabel;

    private Movie movie;
    private LocalDate date;
    private String session;
    private int regularTickets;
    private int discountedTickets;
    private String hall;
    private List<Product> selectedProducts;

    /**
     * Initializes the controller. Invoked automatically by the FXML loader.
     */
    @FXML
    public void initialize() {

    }

    /**
     * Sets the selected movie.
     *
     * @param movie The movie to set.
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Sets the selected date.
     *
     * @param date The date to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Sets the selected session time.
     *
     * @param session The session time to set.
     */
    public void setSession(String session) {
        this.session = session;
        extractHallFromSession();
        loadHallImage();
        loadAvailableSeats();
        displaySeatInputs();
    }

    /**
     * Sets the number of regular tickets.
     *
     * @param regularTickets The number of regular tickets to set.
     */
    public void setRegularTickets(int regularTickets) {
        this.regularTickets = regularTickets;
    }

    /**
     * Sets the number of discounted tickets.
     *
     * @param discountedTickets The number of discounted tickets to set.
     */
    public void setDiscountedTickets(int discountedTickets) {
        this.discountedTickets = discountedTickets;
    }

    /**
     * Sets the username of the logged-in user.
     *
     * @param username The username to display.
     */
    public void setUsername(String username) {
        this.usernameLabel.setText(username);
    }

    /**
     * Sets the selected products.
     *
     * @param selectedProducts The list of selected products.
     */
    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    /**
     * Retrieves the selected products.
     *
     * @return The list of selected products.
     */
    public List<Product> getProducts() {
        return selectedProducts;
    }

    /**
     * Extracts the hall name from the session string.
     */
    private void extractHallFromSession() {
        if (session != null && session.contains(" - ")) {
            String[] parts = session.split(" - ");
            if (parts.length == 2) {
                this.hall = parts[1].trim();
            }
        }
    }

    /**
     * Loads the image of the selected hall.
     */
    private void loadHallImage() {
        if (hall.equals("Hall 1")) {
            hallImageView.setImage(new Image("/com/example/images/hall1.png"));
        } else if (hall.equals("Hall 2")) {
            hallImageView.setImage(new Image("/com/example/images/hall2.png"));
        }
    }

    /**
     * Loads the available seats for the selected movie, date, and hall.
     */
    private void loadAvailableSeats() {
        DatabaseConnection db = new DatabaseConnection();
        List<Integer> availableSeats = db.getAvailableSeats(movie.getId(), date, hall);
        availableSeatsLabel.setText("Available Seats: " + availableSeats.toString());
    }

    /**
     * Displays input fields for seat numbers based on the ticket count.
     */
    private void displaySeatInputs() {
        int totalTickets = regularTickets + discountedTickets;
        for (int i = 0; i < totalTickets; i++) {
            TextField seatInput = new TextField();
            seatInput.setPromptText("Enter seat number for ticket " + (i + 1));
            seatInput.setPrefWidth(50);

            // Add TextFormatter to allow only positive numbers
            seatInput.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getControlNewText().matches("\\d*")) {
                    return change;
                }
                return null;
            }));

            seatInputVBox.getChildren().add(seatInput);
        }
    }

    /**
     * Handles the "Back" button click, navigating to the previous scene.
     *
     * @param actionEvent The event triggered by the button click.
     */
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

    /**
     * Handles the "Confirm" button click, validating seat selections and
     * navigating to the shopping cart scene.
     *
     * @param actionEvent The event triggered by the button click.
     */
    public void onConfirm(ActionEvent actionEvent) {
        List<Integer> selectedSeats = new ArrayList<>();
        List<Integer> availableSeats = new DatabaseConnection().getAvailableSeats(movie.getId(), date, hall);
        for (javafx.scene.Node node : seatInputVBox.getChildren()) {
            if (node instanceof TextField) {
                TextField seatInput = (TextField) node;
                try {
                    int seatNumber = Integer.parseInt(seatInput.getText());
                    if (!availableSeats.contains(seatNumber)) {
                        showAlert("Invalid Seat Number", "Seat number " + seatNumber + " is not available.");
                        return;
                    }
                    selectedSeats.add(seatNumber);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid seat numbers.");
                    return;
                }
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ShoppingCart.fxml"));
            Parent root = loader.load();

            ShoppingCartController controller = loader.getController();
            controller.setSelectedSeats(selectedSeats);
            controller.setMovie(movie);
            controller.setDate(date);
            controller.setSession(session);
            controller.setUsername(usernameLabel.getText());
            controller.setProducts(selectedProducts);
            controller.setTicketCount(regularTickets);
            controller.setDiscountedTicketCount(discountedTickets);

            Stage stage = (Stage) availableSeatsLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with the specified title and content.
     *
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

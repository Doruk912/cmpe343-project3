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

    @FXML
    public ImageView hallImageView;

    @FXML
    private Label availableSeatsLabel;

    @FXML
    private VBox seatInputVBox;

    @FXML
    private Label usernameLabel;

    private Movie movie;
    private LocalDate date;
    private String session;
    private int regularTickets;
    private int discountedTickets;
    private String hall;
    private List<Product> selectedProducts;

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
        displaySeatInputs();
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

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public List<Product> getProducts() {
        return selectedProducts;
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
        if (hall.equals("Hall 1")) {
            hallImageView.setImage(new Image("/com/example/images/hall1.png"));
        } else if (hall.equals("Hall 2")) {
            hallImageView.setImage(new Image("/com/example/images/hall2.png"));
        }
    }

    private void loadAvailableSeats() {
        DatabaseConnection db = new DatabaseConnection();
        List<Integer> availableSeats = db.getAvailableSeats(movie.getId(), date, hall);
        availableSeatsLabel.setText("Available Seats: " + availableSeats.toString());
    }

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

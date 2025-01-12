/**
 * Controller class for managing ticket selection in the application.
 * Handles ticket selection, product management, and navigation to other views.
 */
package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Product;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketSelectionController {

    /** Label for displaying the username. */
    public Label usernameLabel;

    /** Label for displaying the movie title. */
    public Label movieLabel;

    /** Label for displaying the selected date. */
    public Label dateLabel;

    /** Label for displaying the session time. */
    public Label sessionLabel;

    /** Label for displaying the regular ticket price. */
    public Label regularTicketPriceLabel;

    /** Label for displaying the discounted ticket price. */
    public Label discountedTicketPriceLabel;

    /** Spinner for selecting the number of regular tickets. */
    public Spinner<Integer> regularTicketsSpinner;

    /** Spinner for selecting the number of discounted tickets. */
    public Spinner<Integer> discountedTicketsSpinner;

    /** ListView for displaying available products. */
    public ListView<Product> productsListView;

    /** VBox for dynamically adding product controls. */
    public VBox productsVBox;

    private Movie movie;
    private LocalDate date;
    private String session;
    private List<Product> selectedProducts = new ArrayList<>();

    /**
     * Sets the movie details and updates the UI.
     * @param movie The movie to set.
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
        movieLabel.setText(movie.getTitle());
    }

    /**
     * Sets the selected date and updates the UI.
     * @param date The date to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
        dateLabel.setText(date.toString());
    }

    /**
     * Sets the session time and updates the UI.
     * @param session The session time to set.
     */
    public void setSession(String session) {
        this.session = session;
        sessionLabel.setText(session);
    }

    /**
     * Sets the username and updates the UI.
     * @param username The username to set.
     */
    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    /**
     * Initializes the controller and its components.
     * Configures spinners and loads ticket prices and products.
     */
    @FXML
    public void initialize() {
        regularTicketsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        discountedTicketsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));

        loadTicketPrices();
        loadProducts();
    }

    /**
     * Loads ticket prices from the database and updates the UI.
     */
    private void loadTicketPrices() {
        DatabaseConnection db = new DatabaseConnection();
        BigDecimal ticketPrice = db.getTicketPrice();
        BigDecimal discountPercentage = db.getDiscountPercentage();

        DecimalFormat df = new DecimalFormat("#.00");

        regularTicketPriceLabel.setText("$" + df.format(ticketPrice));
        discountedTicketPriceLabel.setText("$" + df.format(ticketPrice.multiply(BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100))))));
    }

    /**
     * Loads available products from the database and populates the UI.
     */
    private void loadProducts() {
        DatabaseConnection db = new DatabaseConnection();
        List<Product> products = db.getAllProducts();

        for (Product product : products) {
            Label productLabel = new Label(product.getName() + " - $" + product.getPrice());
            Spinner<Integer> productSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
            productSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue > 0) {
                    if (!selectedProducts.contains(product)) {
                        selectedProducts.add(product);
                    }
                } else {
                    selectedProducts.remove(product);
                }
            });

            HBox productBox = new HBox(10, productLabel, productSpinner);
            HBox.setHgrow(productLabel, Priority.ALWAYS);
            productLabel.setMaxWidth(Double.MAX_VALUE);
            productsVBox.getChildren().add(productBox);
        }
    }

    /**
     * Handles the "Back" button action to navigate to the Cashier Menu.
     * @param actionEvent The event triggered by the button.
     */
    public void onBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/CashierMenu.fxml"));
            Parent root = loader.load();

            CashierMenuController controller = loader.getController();
            controller.setUsername(usernameLabel.getText());

            Stage stage = (Stage) movieLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Continue" button action to navigate to the Seat Selection view.
     * Validates ticket selection before proceeding.
     * @param actionEvent The event triggered by the button.
     */
    public void onContinue(ActionEvent actionEvent) {
        int regularTickets = regularTicketsSpinner.getValue();
        int discountedTickets = discountedTicketsSpinner.getValue();

        if (regularTickets == 0 && discountedTickets == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Tickets Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one ticket.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/SeatSelection.fxml"));
            Parent root = loader.load();

            SeatSelectionController controller = loader.getController();
            controller.setMovie(movie);
            controller.setDate(date);
            controller.setRegularTickets(regularTickets);
            controller.setDiscountedTickets(discountedTickets);
            controller.setSession(session);
            controller.setUsername(usernameLabel.getText());
            controller.setSelectedProducts(selectedProducts);

            Stage stage = (Stage) movieLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

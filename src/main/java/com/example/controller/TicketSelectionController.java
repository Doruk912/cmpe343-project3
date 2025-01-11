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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketSelectionController {
    public Label usernameLabel;
    public Label movieLabel;
    public Label dateLabel;
    public Label sessionLabel;
    public Label regularTicketPriceLabel;
    public Label discountedTicketPriceLabel;
    public Spinner<Integer> regularTicketsSpinner;
    public Spinner<Integer> discountedTicketsSpinner;
    public ListView<Product> productsListView;
    public VBox productsVBox;

    private Movie movie;
    private LocalDate date;
    private String session;
    private Map<Product, Spinner<Integer>> productSpinners = new HashMap<>();

    public void setMovie(Movie movie) {
        this.movie = movie;
        movieLabel.setText(movie.getTitle());
    }

    public void setDate(LocalDate date) {
        this.date = date;
        dateLabel.setText(date.toString());
    }

    public void setSession(String session) {
        this.session = session;
        sessionLabel.setText(session);
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    @FXML
    public void initialize() {
        regularTicketsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        discountedTicketsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));

        loadTicketPrices();
        loadProducts();
    }

    private void loadTicketPrices() {
        DatabaseConnection db = new DatabaseConnection();
        BigDecimal ticketPrice = db.getTicketPrice();
        BigDecimal discountPercentage = db.getDiscountPercentage();

        DecimalFormat df = new DecimalFormat("#.00");

        regularTicketPriceLabel.setText("$" + df.format(ticketPrice));
        discountedTicketPriceLabel.setText("$" + df.format(ticketPrice.multiply(BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100))))));
    }

    private void loadProducts() {
        DatabaseConnection db = new DatabaseConnection();
        List<Product> products = db.getAllProducts();

        for (Product product : products) {
            Label productLabel = new Label(product.getName() + " - $" + product.getPrice());
            Spinner<Integer> productSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
            productSpinners.put(product, productSpinner);

            HBox productBox = new HBox(10, productLabel, productSpinner);
            HBox.setHgrow(productLabel, Priority.ALWAYS);
            productLabel.setMaxWidth(Double.MAX_VALUE);
            productsVBox.getChildren().add(productBox);
        }
    }

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

            Stage stage = (Stage) movieLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

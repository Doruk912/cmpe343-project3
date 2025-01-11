package com.example.controller;

import com.example.database.DatabaseConnection;
import com.example.model.Movie;
import com.example.model.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ShoppingCartController {

    @FXML
    private Label movieLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label sessionLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<CartItem> cartTableView;

    @FXML
    private TableColumn<CartItem, String> nameColumn;

    @FXML
    private TableColumn<CartItem, Integer> amountColumn;

    @FXML
    private TableColumn<CartItem, Double> priceColumn;

    @FXML
    private TableColumn<CartItem, Double> vatColumn;

    private List<Integer> selectedSeats;
    private List<Product> products;
    private Movie movie;
    private LocalDate date;
    private String session;
    private int ticketCount;
    private int discountedTicketCount;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        vatColumn.setCellValueFactory(cellData -> cellData.getValue().vatProperty().asObject());
    }

    public void setSelectedSeats(List<Integer> selectedSeats) {
        this.selectedSeats = selectedSeats;
        updateCartTableView();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        updateCartTableView();
    }

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
        this.usernameLabel.setText(username);
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
        updateCartTableView();
    }

    public void setDiscountedTicketCount(int discountedTicketCount) {
        this.discountedTicketCount = discountedTicketCount;
        updateCartTableView();
    }

    private void updateCartTableView() {
        DatabaseConnection db = new DatabaseConnection();
        cartTableView.getItems().clear();
        BigDecimal regularTicketPrice = db.getTicketPrice();
        BigDecimal discountedTicketPrice = regularTicketPrice.multiply(BigDecimal.ONE.subtract(db.getDiscountPercentage().divide(BigDecimal.valueOf(100))));
        double ticketVatRate = 0.20;
        double productVatRate = 0.10;

        if (ticketCount > 0) {
            double totalPrice = regularTicketPrice.doubleValue() * ticketCount;
            double vat = totalPrice * ticketVatRate;
            cartTableView.getItems().add(new CartItem("Movie Ticket", ticketCount, totalPrice, vat));
        }
        if (discountedTicketCount > 0) {
            double totalPrice = discountedTicketPrice.doubleValue() * discountedTicketCount;
            double vat = totalPrice * ticketVatRate;
            cartTableView.getItems().add(new CartItem("Discounted Movie Ticket", discountedTicketCount, totalPrice, vat));
        }
        if (products != null) {
            for (Product product : products) {
                double totalPrice = product.getPrice().doubleValue();
                double vat = totalPrice * productVatRate;
                cartTableView.getItems().add(new CartItem(product.getName(), 1, totalPrice, vat));
            }
        }
    }

    public void onCheckout(ActionEvent actionEvent) {
        // Implement checkout logic here
    }

    public void onBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/SeatSelection.fxml"));
            Parent root = loader.load();

            SeatSelectionController controller = loader.getController();
            controller.setMovie(movie);
            controller.setDate(date);
            controller.setSession(session);
            controller.setRegularTickets(ticketCount);
            controller.setDiscountedTickets(discountedTicketCount);
            controller.setUsername(usernameLabel.getText());
            controller.setSelectedProducts(products);

            Stage stage = (Stage) movieLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CartItem {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty amount;
        private final SimpleDoubleProperty price;
        private final SimpleDoubleProperty vat;

        public CartItem(String name, int amount, double price, double vat) {
            this.name = new SimpleStringProperty(name);
            this.amount = new SimpleIntegerProperty(amount);
            this.price = new SimpleDoubleProperty(price);
            this.vat = new SimpleDoubleProperty(vat);
        }

        public String getName() {
            return name.get();
        }

        public int getAmount() {
            return amount.get();
        }

        public double getPrice() {
            return price.get();
        }

        public double getVat() {
            return vat.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public SimpleDoubleProperty priceProperty() {
            return price;
        }

        public SimpleDoubleProperty vatProperty() {
            return vat;
        }
    }
}
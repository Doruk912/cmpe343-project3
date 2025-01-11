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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TicketSelectionController {
    public Label usernameLabel;
    public Label movieLabel;
    public Label dateLabel;
    public Label sessionLabel;
    public Spinner<Integer> regularTicketsSpinner;
    public Spinner<Integer> discountedTicketsSpinner;
    public ListView<Product> productsListView;

    private Movie movie;
    private LocalDate date;
    private String session;

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

        loadProducts();
    }

    private void loadProducts() {
        DatabaseConnection db = new DatabaseConnection();
        List<Product> products = db.getAllProducts();
        productsListView.setItems(FXCollections.observableArrayList(products));
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
    }
}

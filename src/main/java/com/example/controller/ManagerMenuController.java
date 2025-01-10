package com.example.controller;

import com.example.MainApplication;
import com.example.database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class ManagerMenuController {

    @FXML
    private Label usernameLabel;

    public void handleManageInventory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ProductTable.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Product Inventory");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleManagePersonnel(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/PersonnelTable.fxml"));
            Parent root = loader.load();

            PersonnelTableController personnelTableController = loader.getController();
            personnelTableController.setCurrentUser(usernameLabel.getText().replace("Logged in as: ", ""));

            Stage stage = new Stage();
            stage.setTitle("Personnel List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleEditPrices(ActionEvent actionEvent) {
        DatabaseConnection db = new DatabaseConnection();
        BigDecimal currentTicketPrice = db.getTicketPrice();
        BigDecimal currentAgeBasedDiscountRate = db.getDiscountRate();

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Prices");
        dialog.setHeaderText("Update ticket price and discount rate:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField ticketPriceField = new TextField(String.valueOf(currentTicketPrice));
        TextField discountRateField = new TextField(String.valueOf(currentAgeBasedDiscountRate));

        grid.add(new Label("Ticket Price:"), 0, 0);
        grid.add(ticketPriceField, 1, 0);
        grid.add(new Label("Discount Rate (%):"), 0, 1);
        grid.add(discountRateField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    BigDecimal newTicketPrice = new BigDecimal(ticketPriceField.getText().trim());
                    BigDecimal newDiscountRate = new BigDecimal(discountRateField.getText().trim());

                    if (newDiscountRate.compareTo(BigDecimal.ZERO) < 0 || newDiscountRate.compareTo(new BigDecimal("99")) > 0) {
                        showAlert("Invalid Input", "Discount rate must be between 0 and 99.");
                    } else if (newTicketPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        showAlert("Invalid Input", "Ticket price must be greater than 0.");
                    } else if (db.updatePrices(newTicketPrice, newDiscountRate)) {
                        showInfo("Success", "Prices updated successfully!");
                    }else {
                        showAlert("Error", "Failed to update prices.");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter valid numbers.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void setUsername(String username) {
        usernameLabel.setText("Logged in as: " + username);
    }

    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
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

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

/**
 * Controller class for managing the manager menu in the application.
 * This class handles various actions such as managing inventory, managing personnel, editing prices, and logging out.
 */
public class ManagerMenuController {

    @FXML
    private Label usernameLabel;

    /**
     * Handles the action to open the product inventory management window.
     * It loads the ProductTable.fxml file and displays it in a new stage.
     *
     * @param actionEvent the event triggered by the user action.
     */
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

    /**
     * Handles the action to open the personnel management window.
     * It loads the PersonnelTable.fxml file, sets the current user, and displays it in a new stage.
     *
     * @param actionEvent the event triggered by the user action.
     */
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

    /**
     * Handles the action to edit the ticket prices and discount rates.
     * It opens a dialog to allow the user to enter new ticket prices and discount rates.
     * If the values are valid, they will be updated in the database.
     *
     * @param actionEvent the event triggered by the user action.
     */
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

    /**
     * Sets the username to be displayed on the manager's menu.
     *
     * @param username the username of the logged-in user.
     */
    public void setUsername(String username) {
        usernameLabel.setText("Logged in as: " + username);
    }

    /**
     * Handles the action to log out the user and navigate to the login screen.
     * It calls the MainApplication class to display the login screen.
     *
     * @param actionEvent the event triggered by the user action.
     */
    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }

    /**
     * Displays an alert dialog with a warning message.
     *
     * @param title   the title of the alert dialog.
     * @param content the content message of the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays an informational dialog with a success message.
     *
     * @param title   the title of the information dialog.
     * @param content the content message of the information dialog.
     */
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

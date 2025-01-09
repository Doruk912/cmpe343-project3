package com.example.controller;

import com.example.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
            Stage stage = new Stage();
            stage.setTitle("Personnel List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleEditPrices(ActionEvent actionEvent) {
    }

    public void setUsername(String username) {
        usernameLabel.setText("Logged in as: " + username);
    }

    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }
}

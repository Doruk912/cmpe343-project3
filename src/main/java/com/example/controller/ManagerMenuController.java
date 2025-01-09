package com.example.controller;

import com.example.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ManagerMenuController {

    @FXML
    private Label usernameLabel;

    public void handleEditPrices(ActionEvent actionEvent) {
    }

    public void handleManageInventory(ActionEvent actionEvent) {
    }

    public void handleManagePersonnel(ActionEvent actionEvent) {
    }

    public void setUsername(String username) {
        usernameLabel.setText("Logged in as: " + username);
    }

    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }
}

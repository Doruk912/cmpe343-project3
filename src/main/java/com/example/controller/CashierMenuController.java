package com.example.controller;

import com.example.MainApplication;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CashierMenuController {
    public Label usernameLabel;
    public ListView movieListView;
    public ComboBox dateComboBox;
    public ComboBox sessionComboBox;

    public void onLogout(ActionEvent actionEvent) {
        MainApplication app = new MainApplication();
        app.showLoginScreen((Stage) usernameLabel.getScene().getWindow());
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void onContinue(ActionEvent actionEvent) {
    }
}

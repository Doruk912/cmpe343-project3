package com.example.controller;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.database.DatabaseConnection;
import com.example.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    private final DatabaseConnection db = new DatabaseConnection();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Validation Error", "Please fill in both username and password fields.");
            return;
        }

        User user = db.getUser(username);
        if (user == null || !verifyPassword(password, user.getPassword())) {
            showError("Authentication Error", "Invalid username or password.");
            return;
        }

        showInfo("Login Successful", "Welcome, " + user.getUsername() + "!");
        // Proceed to the next screen or application logic.
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public User getAuthenticatedUser(String username, String password) {
        User user = db.getUser(username);
        if(user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}

package com.example.controller;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.database.DatabaseConnection;
import com.example.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

/**
 * Controller class for managing the login process in the application.
 * This class handles user login validation, password verification, and success handling.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private final DatabaseConnection db = new DatabaseConnection();
    private Consumer<User> onLoginSuccess;

    /**
     * Handles the login action by validating the input credentials and verifying the password.
     * If the credentials are valid, the onLoginSuccess callback is invoked.
     */
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

        if(onLoginSuccess != null) {
            onLoginSuccess.accept(user);
        }
    }

    /**
     * Sets the callback to be invoked when the login is successful.
     *
     * @param onLoginSuccess the callback to execute when login is successful.
     */
    public void setOnLoginSuccess(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param title   the title of the alert dialog.
     * @param message the content message of the alert dialog.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an informational message in an alert dialog.
     *
     * @param title   the title of the information dialog.
     * @param message the content message of the information dialog.
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Verifies if the provided password matches the stored hashed password.
     *
     * @param password      the plain-text password entered by the user.
     * @param hashedPassword the hashed password stored in the database.
     * @return true if the password matches, false otherwise.
     */
    private boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}

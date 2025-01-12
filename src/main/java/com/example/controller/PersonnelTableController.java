/**
 * Controller class for managing a personnel table in a JavaFX application.
 * This class handles operations such as adding, editing, removing personnel,
 * and provides interaction with the underlying database for managing users.
 */
package com.example.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.database.DatabaseConnection;
import com.example.model.Product;
import com.example.model.Role;
import com.example.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class PersonnelTableController {

    @FXML
    private TableView<User> personnelTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> username;

    @FXML
    private TableColumn<User, Role> roleColumn;

    private final ObservableList<User> personnelList = FXCollections.observableArrayList();

    private String currentUser;

    /**
     * Initializes the personnel table by setting cell value factories
     * and loading personnel from the database.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        DatabaseConnection db = new DatabaseConnection();
        List<User> personnel = db.getAllUsers();
        personnelList.setAll(personnel);
        personnelTable.setItems(personnelList);
    }

    /**
     * Handles the addition of a new personnel. Opens a dialog for user input,
     * validates the data, and adds the personnel to the database and table if valid.
     *
     * @param actionEvent the event triggered by the user interaction.
     */
    public void handleAddPersonnel(ActionEvent actionEvent) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New Personnel");
        dialog.setHeaderText("Enter personnel details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField usernameField = new TextField();
        TextField nameField = new TextField();
        TextField roleField = new TextField();
        TextField passwordField = new TextField();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String username = usernameField.getText().trim();
                String name = nameField.getText().trim();
                String password = hashPassword(passwordField.getText().trim());
                Role role;
                try {
                    role = Role.valueOf(roleField.getText().trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid Input", "Role must be a valid value.");
                    return null;
                }

                if (!username.isEmpty() && !name.isEmpty() && !password.isEmpty()) {
                    return new User(username, name, password, role);
                } else {
                    showAlert("Invalid Input", "All fields must be filled with valid values.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.addUser(user)) {
                List<User> personnel = db.getAllUsers();
                personnelList.setAll(personnel);
                showInfo("Success", "Personnel added successfully!");
            } else {
                showAlert("Error", "Failed to add the personnel.");
            }
        });
    }

    /**
     * Handles the editing of an existing personnel. Opens a dialog for modifying personnel details,
     * validates the changes, and updates the database and table if valid.
     *
     * @param actionEvent the event triggered by the user interaction.
     */
    public void handleEditPersonnel(ActionEvent actionEvent) {
        User selectedUser = personnelTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("No Selection", "Please select a personnel to edit.");
            return;
        }

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit Personnel");
        dialog.setHeaderText("Edit personnel details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPrefWidth(275);

        TextField usernameField = new TextField(selectedUser.getUsername());
        usernameField.setPrefWidth(200);
        TextField nameField = new TextField(selectedUser.getName());
        nameField.setPrefWidth(200);
        TextField roleField = new TextField(selectedUser.getRole().toString());
        roleField.setPrefWidth(200);
        TextField passwordField = new TextField();
        passwordField.setPrefWidth(200);
        passwordField.setPromptText("Blank to keep current password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String username = usernameField.getText().trim();
                String name = nameField.getText().trim();
                String password = passwordField.getText().trim().isEmpty() ? selectedUser.getPassword() : hashPassword(passwordField.getText().trim());
                Role role;
                try {
                    role = Role.valueOf(roleField.getText().trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid Input", "Role must be a valid value.");
                    return null;
                }

                if (!username.isEmpty() && !name.isEmpty() && !password.isEmpty()) {
                    selectedUser.setUsername(username);
                    selectedUser.setName(name);
                    selectedUser.setPassword(password);
                    selectedUser.setRole(role);
                    return selectedUser;
                } else {
                    showAlert("Invalid Input", "All fields must be filled with valid values.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            DatabaseConnection db = new DatabaseConnection();
            if (db.updateUser(user)) {
                personnelTable.refresh();
                showInfo("Success", "Personnel updated successfully!");
            } else {
                showAlert("Error", "Failed to update the personnel.");
            }
        });
    }

    /**
     * Handles the removal of a selected personnel. Prompts the user for confirmation
     * and removes the personnel from the database and table upon confirmation.
     *
     * @param actionEvent the event triggered by the user interaction.
     */
    public void handleRemovePersonnel(ActionEvent actionEvent) {
        User selectedUser = personnelTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("No Selection", "Please select a personnel to remove.");
            return;
        }

        if (selectedUser.getUsername().equals(currentUser)) {
            showAlert("Error", "Cannot remove yourself.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Personnel");
        alert.setHeaderText("Are you sure you want to remove the selected personnel?");
        alert.setContentText(selectedUser.getUsername() + " will be removed.");

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                DatabaseConnection db = new DatabaseConnection();
                if (db.removeUser(selectedUser)) {
                    personnelList.remove(selectedUser);
                    showInfo("Success", "Personnel removed successfully!");
                } else {
                    showAlert("Error", "Failed to remove the personnel.");
                }
            }
        });
    }

    /**
     * Closes the current application stage.
     *
     * @param actionEvent the event triggered by the user interaction.
     */
    public void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) personnelTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays a warning alert dialog with the given title and message.
     *
     * @param title   the title of the alert.
     * @param content the message content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays an informational alert dialog with the given title and message.
     *
     * @param title   the title of the alert.
     * @param content the message content of the alert.
     */
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Hashes the given password using BCrypt.
     *
     * @param password the plain text password to hash.
     * @return the hashed password as a string.
     */
    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Sets the current logged-in user.
     *
     * @param currentUser the username of the current logged-in user.
     */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}

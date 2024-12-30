package com.example.view;

import com.example.controller.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class LoginView extends Application {

    private final LoginController loginController = new LoginController();

    @Override
    public void start(Stage primaryStage) {

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (loginController.getAuthenticatedUser(username, password) != null) {
                openMenu(primaryStage);
            } else {
                System.out.println("Invalid credentials");
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);

        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setTitle("Login Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openMenu(Stage primaryStage) {
        Label menuLabel = new Label("Welcome to the Menu!");
        Scene menuScene = new Scene(menuLabel, 300, 200);
        primaryStage.setScene(menuScene);
    }
}
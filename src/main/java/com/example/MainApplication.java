package com.example;

import com.example.controller.LoginController;
import com.example.controller.SplashController;
import com.example.model.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/splash.fxml"));
            StackPane splashRoot = loader.load();
            Scene splashScene = new Scene(splashRoot);

            primaryStage.setScene(splashScene);
            primaryStage.setTitle("Cinema Application");
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/images/logo3.png"))));
            primaryStage.setResizable(false);
            primaryStage.show();

            SplashController splashController = loader.getController();
            splashController.playAnimation(() -> showLoginScreen(primaryStage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoginScreen(Stage primaryStage) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);

            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Cinema Application Login");
            primaryStage.show();

            LoginController loginController = loader.getController();
            loginController.setOnLoginSuccess(user -> handleRoleSpecificMenu(primaryStage, user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRoleSpecificMenu(Stage stage, User user){
        String fxmlPath;
        String title;
        switch (user.getRole()) {
            case CASHIER:
                fxmlPath = "/com/example/CashierMenu.fxml";
                title = "Cashier Menu";
                break;
            case ADMIN:
                fxmlPath = "/com/example/AdminMenu.fxml";
                title = "Admin Menu";
                break;
            case MANAGER:
                fxmlPath = "/com/example/ManagerMenu.fxml";
                title = "Manager Menu";
                break;
            default:
                showError("Role Error", "Invalid user role: " + user.getRole());
                return;
        }
        showScene(stage, fxmlPath, title);
    }

    private void showScene(Stage primaryStage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (Exception e) {
            showError("Scene Error", "Failed to load the scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//mvn javafx:run
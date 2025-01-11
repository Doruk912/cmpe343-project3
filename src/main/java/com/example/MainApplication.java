package com.example;

import com.example.controller.*;
import com.example.model.Movie;
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

import java.io.IOException;
import java.time.LocalDate;
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

    public void showLoginScreen(Stage primaryStage) {
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
                title = "Cinema Center - Cashier Menu";
                break;
            case ADMIN:
                fxmlPath = "/com/example/AdminMenu.fxml";
                title = "Cinema Center - Admin Menu";
                break;
            case MANAGER:
                fxmlPath = "/com/example/ManagerMenu.fxml";
                title = "Cinema Center - Manager Menu";
                break;
            default:
                showError("Role Error", "Invalid user role: " + user.getRole());
                return;
        }
        showScene(stage, fxmlPath, title, user.getUsername());
    }

    private void showScene(Stage primaryStage, String fxmlPath, String title, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.centerOnScreen();
            primaryStage.show();

            if (fxmlPath.equals("/com/example/AdminMenu.fxml")) {
                AdminMenuController controller = loader.getController();
                controller.setUsername(username);
            }else if (fxmlPath.equals("/com/example/ManagerMenu.fxml")) {
                ManagerMenuController controller = loader.getController();
                controller.setUsername(username);
            }else if (fxmlPath.equals("/com/example/CashierMenu.fxml")) {
                CashierMenuController controller = loader.getController();
                controller.setUsername(username);
            }
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

    public void showTicketSelectionScreen(Stage stage, Movie movie, LocalDate date, String session, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/TicketSelection.fxml"));
            Parent root = loader.load();

            TicketSelectionController controller = loader.getController();
            controller.setMovie(movie);
            controller.setDate(date);
            controller.setSession(session);
            controller.setUsername(username);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//mvn javafx:run
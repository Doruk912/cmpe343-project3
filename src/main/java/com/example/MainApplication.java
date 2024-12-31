package com.example;

import com.example.controller.SplashController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/splash.fxml"));
            StackPane splashRoot = loader.load();
            Scene splashScene = new Scene(splashRoot);

            primaryStage.setScene(splashScene);
            primaryStage.setTitle("Cinema Application");
            primaryStage.show();

            SplashController splashController = loader.getController();
            splashController.playAnimation(() -> showLoginScreen(primaryStage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showLoginScreen(Stage primaryStage) {
        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/example/login.fxml"));
            Scene loginScene = new Scene(loginLoader.load());

            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login - Cinema Application");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//mvn javafx:run
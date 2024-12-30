package com.example;

import com.example.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginView loginScreen = new LoginView();
        loginScreen.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//mvn javafx:run
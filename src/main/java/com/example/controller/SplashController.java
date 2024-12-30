package com.example.controller;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class SplashController {

    @FXML
    private ImageView logo1;

    @FXML
    private ImageView logo2;

    @FXML
    private ImageView logo3;

    @FXML
    private Label welcomeLabel;

    private Runnable onAnimationFinished;

    public void initialize() {
        logo1.setImage(new Image(getClass().getResource("/com/example/images/logo1.png").toExternalForm()));
        logo2.setImage(new Image(getClass().getResource("/com/example/images/logo2.png").toExternalForm()));
        logo3.setImage(new Image(getClass().getResource("/com/example/images/logo3.png").toExternalForm()));

        SequentialTransition logo1Transition = createFadeInOutTransition(logo1);
        SequentialTransition logo2Transition = createFadeInOutTransition(logo2);
        SequentialTransition logo3Transition = createFadeInOutTransition(logo3);

        FadeTransition fadeLabel = new FadeTransition(Duration.seconds(2), welcomeLabel);
        fadeLabel.setFromValue(0);
        fadeLabel.setToValue(1);

        SequentialTransition masterTransition = new SequentialTransition(
                logo1Transition, logo2Transition, logo3Transition, fadeLabel
        );

        masterTransition.setOnFinished(event -> {
            if (onAnimationFinished != null) {
                onAnimationFinished.run();
            }
        });

        masterTransition.play();
    }

    private SequentialTransition createFadeInOutTransition(ImageView logo) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), logo);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), logo);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        return new SequentialTransition(fadeIn, fadeOut);
    }

    public void playAnimation(Runnable onAnimationFinished) {
        this.onAnimationFinished = onAnimationFinished;
    }
}

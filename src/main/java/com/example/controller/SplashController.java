/**
 * Controller class for managing the splash screen animations.
 * Handles sequential animations for logos and a welcome label.
 */
package com.example.controller;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class SplashController {

    /** ImageView for the first logo. */
    @FXML
    private ImageView logo1;

    /** ImageView for the second logo. */
    @FXML
    private ImageView logo2;

    /** ImageView for the third logo. */
    @FXML
    private ImageView logo3;

    /** Label for the welcome message. */
    @FXML
    private Label welcomeLabel;

    /** Runnable to execute when the animation finishes. */
    private Runnable onAnimationFinished;

    /**
     * Initializes the splash screen by setting up images and animations.
     * Plays a sequence of fade-in and fade-out transitions for the logos
     * followed by a fade-in animation for the welcome label.
     */
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

    /**
     * Creates a sequential fade-in and fade-out transition for the specified logo.
     * @param logo The ImageView to animate.
     * @return A SequentialTransition for the fade-in and fade-out animation.
     */
    private SequentialTransition createFadeInOutTransition(ImageView logo) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), logo);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), logo);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        return new SequentialTransition(fadeIn, fadeOut);
    }

    /**
     * Plays the splash screen animation and sets a callback to execute when the animation finishes.
     * @param onAnimationFinished A Runnable to execute after the animation completes.
     */
    public void playAnimation(Runnable onAnimationFinished) {
        this.onAnimationFinished = onAnimationFinished;
    }
}

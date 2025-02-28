package com.proj.util;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import java.util.Objects;

public class VideoPlayer {
    private MediaPlayer mediaPlayer;
    private boolean initialized;

    public VideoPlayer() {
        // Initialize JavaFX toolkit
        new JFXPanel(); // this will initialize JavaFX environment
        initialized = true;
    }

    public void playVideo(String videoPath) {
        if (!initialized) {
            System.err.println("JavaFX not initialized!");
            return;
        }

        Platform.runLater(() -> {
            try {
                String path = Objects.requireNonNull(getClass().getResource(videoPath)).toExternalForm();
                Media media = new Media(path);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);

                // Create a new stage and scene for the video
                Stage stage = new Stage();
                MediaView mediaView = new MediaView(mediaPlayer);
                Scene scene = new Scene(new Group(mediaView));
                stage.setScene(scene);
                stage.show();

                // Handle completion
                mediaPlayer.setOnEndOfMedia(() -> {
                    stage.close();
                    mediaPlayer.dispose();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }
}

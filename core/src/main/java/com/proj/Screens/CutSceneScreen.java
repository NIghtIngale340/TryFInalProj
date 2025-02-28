package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class CutSceneScreen extends ScreenAdapter {
    private final TechXplorerGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private BitmapFont font;

    private Array<String> dialogues;
    private int currentDialogue = 0;

    private float timeElapsed = 0f;
    private Texture introTexture;
    private boolean cutsceneFinished = false;

    // Video playback components
    private MediaPlayer mediaPlayer;
    private boolean isVideoPlaying = true;
    private boolean videoError = false;
    private static boolean jfxInitialized = false;

    // JavaFX components for video rendering
    private JFrame videoFrame;
    private Canvas videoCanvas;
    private MediaView mediaView;

    public CutSceneScreen(TechXplorerGame game) {
        this.game = game;
        this.batch = game.getBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT, camera);

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        // Initialize dialogue text
        dialogues = new Array<>();
        dialogues.add("In the year 21XX, robots have taken over the world...");
        dialogues.add("DemoniTech created a powerful anti-AI robot named CPN...");
        dialogues.add("But EXODUS attacked the lab, destroyed CPN, and scattered its parts...");
        dialogues.add("Dr. Fox's child must now recover CPN's lost pieces...");
        dialogues.add("Battle robot bosses and stop EXODUS from taking over the world!");

        // Load fallback texture
        introTexture = game.getAssetManager().get("cutscene/intro.png", Texture.class);

        // Initialize JavaFX and video playback
        initializeVideo();
    }

    private void initializeVideo() {
        try {
            // Use System property to fix module error
            System.setProperty("javafx.preloader", "com.sun.javafx.application.LauncherImpl");
            System.setProperty("java.util.logging.config.file", "logging.properties");

            // Initialize JavaFX if not already initialized
            if (!jfxInitialized) {
                final JFXPanel fxPanel = new JFXPanel(); // Initialize JavaFX platform
                jfxInitialized = true;
            }

            // Get the video file path
            String videoPath = Gdx.files.internal("assets/video/intro.mp4").file().getAbsolutePath();
            File videoFile = new File(videoPath);

            if (!videoFile.exists()) {
                Gdx.app.error("CutSceneScreen", "Video file not found: " + videoPath);
                videoError = true;
                isVideoPlaying = false;
                return;
            }

            // Create a separate frame for video display
            SwingUtilities.invokeLater(() -> {
                videoFrame = new JFrame("Video");
                videoFrame.setSize(800, 480);
                videoFrame.setLocationRelativeTo(null);
                videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JFXPanel videoPanel = new JFXPanel();
                videoFrame.add(videoPanel);
                videoFrame.setVisible(true);

                // Create Media and MediaPlayer on the JavaFX thread
                Platform.runLater(() -> {
                    try {
                        Group root = new Group();
                        Scene scene = new Scene(root);
                        videoPanel.setScene(scene);

                        Media media = new Media(videoFile.toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        mediaView = new MediaView(mediaPlayer);

                        // Size the video view
                        mediaView.setFitWidth(800);
                        mediaView.setFitHeight(480);
                        mediaView.setPreserveRatio(true);

                        root.getChildren().add(mediaView);

                        // Set up video completion handler
                        mediaPlayer.setOnEndOfMedia(() -> {
                            isVideoPlaying = false;
                            Platform.runLater(() -> {
                                if (videoFrame != null) {
                                    videoFrame.dispose();
                                    videoFrame = null;
                                }
                                mediaPlayer.dispose();
                            });
                        });

                        // Start playing
                        mediaPlayer.play();
                    } catch (Exception e) {
                        Gdx.app.error("CutSceneScreen", "Error initializing video: " + e.getMessage(), e);
                        videoError = true;
                        isVideoPlaying = false;
                        if (videoFrame != null) {
                            videoFrame.dispose();
                            videoFrame = null;
                        }
                    }
                });
            });
        } catch (Exception e) {
            Gdx.app.error("CutSceneScreen", "Error setting up video: " + e.getMessage(), e);
            videoError = true;
            isVideoPlaying = false;
        }
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update time
        timeElapsed += delta;

        // Process input (skip cutscene)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            if (isVideoPlaying) {
                Platform.runLater(() -> {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.dispose();
                    }
                });
                if (videoFrame != null) {
                    videoFrame.dispose();
                    videoFrame = null;
                }
                isVideoPlaying = false;
            } else {
                nextDialogue();
            }
        }

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (!isVideoPlaying) {
            // Video finished or skipped, show dialogue sequence
            batch.draw(introTexture, 0, 0, TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT);

            // Auto advance dialogue every 5 seconds
            if (timeElapsed > 5f) {
                timeElapsed = 0f;
                nextDialogue();
            }

            // Draw current dialogue text
            if (currentDialogue < dialogues.size) {
                font.draw(batch, dialogues.get(currentDialogue), 50, 100);
                font.draw(batch, "Press SPACE to continue", 50, 50);
            }
        } else if (videoError) {
            // If video failed, show the static image
            batch.draw(introTexture, 0, 0, TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT);
        } else {
            // While video is playing, display a message in the game window
            font.draw(batch, "Playing intro video... Press SPACE to skip",
                TechXplorerGame.WORLD_WIDTH/2 - 150, TechXplorerGame.WORLD_HEIGHT/2);
        }

        batch.end();

        // Check if cutscene is finished
        if (cutsceneFinished) {
            Platform.runLater(() -> {
                if (mediaPlayer != null) {
                    mediaPlayer.dispose();
                }
            });
            if (videoFrame != null) {
                videoFrame.dispose();
                videoFrame = null;
            }
            // Transition to MainMenu instead of GameScreen
            game.goToMainMenu();
        }
    }

    private void nextDialogue() {
        currentDialogue++;
        if (currentDialogue >= dialogues.size) {
            cutsceneFinished = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
            }
        });
        if (videoFrame != null) {
            videoFrame.dispose();
            videoFrame = null;
        }
    }
}

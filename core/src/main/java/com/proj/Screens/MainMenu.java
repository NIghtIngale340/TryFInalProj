package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.assets.AssetDescriptors;
import com.proj.core.TechXplorerGame;

public class MainMenuScreen extends ScreenAdapter {
    private final TechXplorerGame game;
    private final Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Texture backgroundTexture;
    private final Skin skin;

    public MainMenuScreen(TechXplorerGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.getBatch());

        // Load skin for UI elements
        skin = game.getAssetManager().get(AssetDescriptors.UI_SKIN);

        // Load background texture (you might need to add this to your assets)
        backgroundTexture = new Texture(Gdx.files.internal("backgrounds/menu_background.png"));

        // Set up menu UI
        setupUI();

        // Set as input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        // Create a table for our menu items
        Table table = new Table();
        table.setFillParent(true);

        // Create buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton creditsButton = new TextButton("Credits", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add buttons to table with spacing
        table.add(startButton).padBottom(20).fillX().uniformX();
        table.row();
        table.add(settingsButton).padBottom(20).fillX().uniformX();
        table.row();
        table.add(creditsButton).padBottom(20).fillX().uniformX();
        table.row();
        table.add(exitButton).fillX().uniformX();

        // Add listeners to buttons
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Go to character creation screen
                game.setScreen(new CharacterCreationScreen(game));
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: Implement settings screen
                Gdx.app.log("MainMenu", "Settings button clicked");
            }
        });

        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: Implement credits screen
                Gdx.app.log("MainMenu", "Credits button clicked");
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // Add table to stage
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT);
        game.getBatch().end();

        // Update and draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

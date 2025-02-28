package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;
import com.proj.assets.AssetDescriptors;

public class CharacterCreationScreen extends ScreenAdapter {
    private final TechXplorerGame game;
    private final Stage stage;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Skin skin;

    // Character options
    private String playerName = "Player";
    private boolean isMale = true;

    // Preview textures
    private Texture maleTexture;
    private Texture femaleTexture;
    private Image characterPreview;

    public CharacterCreationScreen(TechXplorerGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.getBatch());

        // Load skin for UI elements
        skin = game.getAssetManager().get(AssetDescriptors.UI_SKIN);

        // Load character preview textures (you'll need to add these to your assets)
            maleTexture = new Texture(Gdx.files.internal("sprites/player_male.png"));
        femaleTexture = new Texture(Gdx.files.internal("sprites/player_female.png"));

        // Set up UI
        setupUI();

        // Set as input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // Add title
        Label titleLabel = new Label("Character Creation", skin, "title");
        mainTable.add(titleLabel).colspan(2).pad(20);
        mainTable.row();

        // Character name input
        Label nameLabel = new Label("Character Name:", skin);
        TextField nameField = new TextField(playerName, skin);
        mainTable.add(nameLabel).pad(10).align(Align.right);
        mainTable.add(nameField).pad(10).fillX();
        mainTable.row();

        // Gender selection
        Label genderLabel = new Label("Gender:", skin);

        // Create buttons for gender selection
        final TextButton maleButton = new TextButton("Male", skin, "toggle");
        final TextButton femaleButton = new TextButton("Female", skin, "toggle");

        // Set initial state
        maleButton.setChecked(true);

        // Add listeners
        maleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (maleButton.isChecked()) {
                    femaleButton.setChecked(false);
                    isMale = true;
                    characterPreview.setDrawable(new Image(maleTexture).getDrawable());
                } else {
                    maleButton.setChecked(true); // Ensure one is always selected
                }
            }
        });

        femaleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (femaleButton.isChecked()) {
                    maleButton.setChecked(false);
                    isMale = false;
                    characterPreview.setDrawable(new Image(femaleTexture).getDrawable());
                } else {
                    femaleButton.setChecked(true); // Ensure one is always selected
                }
            }
        });

        // Create a table for gender buttons
        Table genderTable = new Table();
        genderTable.add(maleButton).pad(5);
        genderTable.add(femaleButton).pad(5);

        mainTable.add(genderLabel).pad(10).align(Align.right);
        mainTable.add(genderTable).pad(10).fillX();
        mainTable.row();

        // Character preview
        characterPreview = new Image(maleTexture);
        characterPreview.setSize(128, 128);
        mainTable.add(new Label("Preview:", skin)).align(Align.right);
        mainTable.add(characterPreview).pad(20).size(128, 128);
        mainTable.row();

        // Confirm button
        TextButton confirmButton = new TextButton("Start Adventure", skin);
        mainTable.add(confirmButton).colspan(2).pad(20).fillX();

        // Add listener to confirm button
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerName = nameField.getText();
                if (playerName == null || playerName.trim().isEmpty()) {
                    playerName = "Player";
                }

                // Save character options to game
                game.setPlayerName(playerName);
                game.setPlayerGender(isMale);

                // Start the game
                game.startGameplay();
            }
        });

        // Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        maleTexture.dispose();
        femaleTexture.dispose();
    }
}

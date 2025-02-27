package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;
import com.proj.assets.AssetDescriptors;

public class LoadingScreen extends ScreenAdapter {
    private final TechXplorerGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private BitmapFont font;

    private float progress = 0f;

    public LoadingScreen(TechXplorerGame game) {
        this.game = game;
        this.batch = game.getBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT, camera);

        // Load assets needed for loading screen first
        font = new BitmapFont();
    }

    @Override
    public void show() {
        // Queue all game assets for loading
        queueAssets();
    }

    private void queueAssets() {
        // Queue map assets
        for (int i = 0; i < 5; i++) {
            game.getAssetManager().load("maps/map" + i + ".tmx", com.badlogic.gdx.maps.tiled.TiledMap.class);
        }

        // Queue player and enemy textures
        game.getAssetManager().load("sprites/player.png", com.badlogic.gdx.graphics.Texture.class);

        // Queue boss textures
        game.getAssetManager().load("sprites/boss_steelward.png", com.badlogic.gdx.graphics.Texture.class);
        game.getAssetManager().load("sprites/boss_blazecinder.png", com.badlogic.gdx.graphics.Texture.class);
        game.getAssetManager().load("sprites/boss_memorix.png", com.badlogic.gdx.graphics.Texture.class);
        game.getAssetManager().load("sprites/boss_glitchron.png", com.badlogic.gdx.graphics.Texture.class);
        game.getAssetManager().load("sprites/boss_exodus.png", com.badlogic.gdx.graphics.Texture.class);

        // Queue cutscene assets
        game.getAssetManager().load("cutscene/intro.png", com.badlogic.gdx.graphics.Texture.class);

        // Queue UI elements
        game.getAssetManager().load("ui/ui_skin.json", com.badlogic.gdx.scenes.scene2d.ui.Skin.class);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update progress
        progress = game.getAssetManager().getProgress();

        // Update asset manager
        if (game.getAssetManager().update()) {
            // Loading complete, move to cutscene
            game.startGame();
        }

        // Draw progress bar
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw loading text and progress bar
        font.draw(batch, "Loading... " + Math.round(progress * 100) + "%", 20, TechXplorerGame.WORLD_HEIGHT / 2);

        // Draw progress bar
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}

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
        game.getAssetManager().load(AssetDescriptors.MAP_0);
        game.getAssetManager().load(AssetDescriptors.MAP_1);
        game.getAssetManager().load(AssetDescriptors.MAP_2);
        game.getAssetManager().load(AssetDescriptors.MAP_3);
        game.getAssetManager().load(AssetDescriptors.MAP_0);
        game.getAssetManager().load(AssetDescriptors.MAP_1);
        game.getAssetManager().load(AssetDescriptors.MAP_2);
        game.getAssetManager().load(AssetDescriptors.MAP_3);
        game.getAssetManager().load(AssetDescriptors.MAP_4);

        // Queue player and enemy textures
        game.getAssetManager().load(AssetDescriptors.PLAYER);

        // Queue boss textures
        game.getAssetManager().load(AssetDescriptors.BOSS_STEELWARD);
        game.getAssetManager().load(AssetDescriptors.BOSS_BLAZECINDER);
        game.getAssetManager().load(AssetDescriptors.BOSS_MEMORIX);
        game.getAssetManager().load(AssetDescriptors.BOSS_GLITCHRON);
        game.getAssetManager().load(AssetDescriptors.BOSS_EXODUS);

        // Queue boss background textures
        game.getAssetManager().load(AssetDescriptors.BG_STEELWARD);
        game.getAssetManager().load(AssetDescriptors.BG_BLAZECINDER);
        game.getAssetManager().load(AssetDescriptors.BG_MEMORIX);
        game.getAssetManager().load(AssetDescriptors.BG_GLITCHRON);
        game.getAssetManager().load(AssetDescriptors.BG_EXODUS);

        // Queue cutscene assets
        game.getAssetManager().load(AssetDescriptors.CUTSCENE_INTRO);

        // Queue UI elements
        game.getAssetManager().load(AssetDescriptors.UI_SKIN);
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

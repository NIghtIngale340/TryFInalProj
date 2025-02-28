package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;
import com.proj.map.mapManager;

public class GameScreen implements Screen {
    private final TechXplorerGame game;
    private final AssetManager assetManager;
    private final com.proj.map.mapManager mapManager;

    private OrthographicCamera camera;
    private Viewport viewport;

    // Game objects
    private Vector2 playerPosition;
    private Vector2 bossPosition;
    private Array<Rectangle> collisionObjects;

    public GameScreen(TechXplorerGame game, int mapIndex) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.mapManager = new mapManager(assetManager);

        // Setup camera
        camera = new OrthographicCamera();
        viewport = new FitViewport(320, 320, camera); // Adjust to your game's viewport size

        // Load the map
        mapManager.loadMap(mapIndex);

        // Get player and boss spawn positions
        playerPosition = mapManager.getPlayerSpawnPosition();
        bossPosition = mapManager.getBossSpawnPosition();

        // Get collision objects
        collisionObjects = mapManager.getCollisionObjects();
    }

    @Override
    public void show() {
        // Additional setup if needed when screen becomes visible
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        camera.position.set(playerPosition, 0);
        camera.update();

        // Render the map
        mapManager.render(camera);

        // Render game objects (player, enemies, etc.)
        // ...
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Handle pause if needed
    }

    @Override
    public void resume() {
        // Handle resume if needed
    }

    @Override
    public void hide() {
        // Handle when screen is hidden
    }

    @Override
    public void dispose() {
        mapManager.dispose();
    }
}

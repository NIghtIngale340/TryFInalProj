package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;
import com.proj.map.mapManager;
import com.proj.entities.Player;
import com.proj.entities.Boss;

public class GameScreen implements Screen {
    private final TechXplorerGame game;
    private final AssetManager assetManager;
    private final com.proj.map.mapManager mapManager;
    private final SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;

    // Game objects
    private Player player;
    private Boss boss;
    private Vector2 playerPosition;
    private Vector2 bossPosition;
    private Array<Rectangle> collisionObjects;

    // For tracking input
    private float horizontalInput;
    private float verticalInput;

    public GameScreen(TechXplorerGame game, int mapIndex) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        this.batch = game.getBatch();
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

        // Create player with appropriate gender
        player = new Player(assetManager, playerPosition.x, playerPosition.y, game.isPlayerMale());

        // Create boss for this level
        if (bossPosition != null) {
            boss = Boss.createBoss(mapIndex, assetManager, bossPosition.x, bossPosition.y);
        }
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

        // Process input
        processInput();

        // Update game objects
        player.update(delta, horizontalInput, verticalInput);

        if (boss != null) {
            boss.update(delta);
        }

        // Update camera to follow player
        camera.position.set(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2, 0);
        camera.update();

        // Render the map
        mapManager.render(camera);

        // Render game objects
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Render player
        player.render(batch);

        // Render boss if present
        if (boss != null) {
            boss.render(batch);
        }

        batch.end();
    }

    private void processInput() {
        // Reset input
        horizontalInput = 0;
        verticalInput = 0;

        // Process keyboard input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalInput = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalInput = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            verticalInput = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            verticalInput = -1;
        }
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
        if (player != null) {
            player.dispose();
        }
    }
}

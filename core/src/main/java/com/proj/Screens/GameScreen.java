package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;
import com.proj.entities.Boss;
import com.proj.entities.Player;
import com.proj.quiz.QuizManager;

public class GameScreen extends ScreenAdapter {
    // Constants
    private static final float UNIT_SCALE = 1/16f; // Assuming 16x16 tile size

    // Core components
    private final TechXplorerGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;

    // Map components
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<Rectangle> collisionRectangles;

    // Game entities
    private Player player;
    private Boss currentBoss;

    // Game state
    private boolean inBossBattle = false;
    private QuizManager quizManager;

    public GameScreen(TechXplorerGame game) {
        this.game = game;
        this.batch = game.getBatch();

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT, camera);

        // Load the current map
        loadMap();

        // Set up player and boss
        setupEntities();

        // Set up collision
        setupCollision();

        // Setup input handling
        setupInput();

        // Setup quiz manager for battles
        quizManager = new QuizManager(game.getCurrentMapIndex());
    }

    private void loadMap() {
        // Load the map based on current map index
        int mapIndex = game.getCurrentMapIndex();
        map = game.getAssetManager().get("maps/map" + mapIndex + ".tmx", TiledMap.class);

        // Create the map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE, batch);
    }

    private void setupEntities() {
        // Get player spawn from map
        Vector2 playerSpawn = getSpawnPoint("playerSpawn");
        player = new Player(game.getAssetManager(), playerSpawn.x, playerSpawn.y);

        // Create boss based on the current map
        Vector2 bossSpawn = getSpawnPoint("bossSpawn");
        currentBoss = Boss.createBoss(game.getCurrentMapIndex(), game.getAssetManager(), bossSpawn.x, bossSpawn.y);
    }

    private Vector2 getSpawnPoint(String objectName) {
        // Find spawn point from map objects layer
        MapLayer objectLayer = map.getLayers().get("objects");
        if (objectLayer != null) {
            for (MapObject object : objectLayer.getObjects()) {
                if (objectName.equals(object.getName())) {
                    RectangleMapObject rectObject = (RectangleMapObject) object;
                    Rectangle rect = rectObject.getRectangle();
                    return new Vector2(rect.x * UNIT_SCALE, rect.y * UNIT_SCALE);
                }
            }
        }

        // Default spawn if not found
        return new Vector2(100, 100);
    }

    private void setupCollision() {
        collisionRectangles = new Array<>();

        // Get collision objects from map
        MapLayer collisionLayer = map.getLayers().get("collision");
        if (collisionLayer != null) {
            for (MapObject object : collisionLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    // Scale rectangle to match our unit scale
                    rect.x *= UNIT_SCALE;
                    rect.y *= UNIT_SCALE;
                    rect.width *= UNIT_SCALE;
                    rect.height *= UNIT_SCALE;
                    collisionRectangles.add(rect);
                }
            }
        }
    }

    private void setupInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // Space key triggers boss battle when near the boss
                if (keycode == Input.Keys.SPACE && player.getBounds().overlaps(currentBoss.getBounds())) {
                    startBossBattle();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Touch can also trigger boss battle when near boss
                if (!inBossBattle && player.getBounds().overlaps(currentBoss.getBounds())) {
                    startBossBattle();
                    return true;
                }
                return false;
            }
        });
    }

    private void startBossBattle() {
        inBossBattle = true;
        quizManager.startQuiz();
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update
        if (!inBossBattle) {
            // Update player
            updatePlayer(delta);

            // Update camera to follow player
            updateCamera();
        } else {
            // Update battle
            updateBattle(delta);
        }

        // Render
        if (!inBossBattle) {
            // Render world
            renderWorld();
        } else {
            // Render battle
            renderBattle();
        }
    }

    private void updatePlayer(float delta) {
        // Handle input for player movement
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

        // Calculate movement direction
        float horizontalMovement = 0f;
        float verticalMovement = 0f;

        if (leftPressed) horizontalMovement -= 1;
        if (rightPressed) horizontalMovement += 1;
        if (upPressed) verticalMovement += 1;
        if (downPressed) verticalMovement -= 1;

        // Update player with movement input
        player.update(delta, horizontalMovement, verticalMovement);

        // Check collision with map boundaries and obstacles
        handleCollisions();
    }

    private void handleCollisions() {
        // Check collisions with map obstacles
        Rectangle playerBounds = player.getBounds();

        // Check collision with all obstacles
        for (Rectangle obstacle : collisionRectangles) {
            if (playerBounds.overlaps(obstacle)) {
                // Resolve collision by pushing the player back
                resolveCollision(playerBounds, obstacle);
            }
        }
    }

    private void resolveCollision(Rectangle playerBounds, Rectangle obstacle) {
        // Calculate overlap on each side
        float leftOverlap = playerBounds.x + playerBounds.width - obstacle.x;
        float rightOverlap = obstacle.x + obstacle.width - playerBounds.x;
        float topOverlap = playerBounds.y + playerBounds.height - obstacle.y;
        float bottomOverlap = obstacle.y + obstacle.height - playerBounds.y;

        // Find smallest overlap to determine push direction
        float minOverlapX = Math.min(leftOverlap, rightOverlap);
        float minOverlapY = Math.min(topOverlap, bottomOverlap);

        // Push in the direction of smallest overlap
        if (minOverlapX < minOverlapY) {
            if (leftOverlap < rightOverlap) {
                player.setPosition(obstacle.x - playerBounds.width, player.getY());
            } else {
                player.setPosition(obstacle.x + obstacle.width, player.getY());
            }
        } else {
            if (topOverlap < bottomOverlap) {
                player.setPosition(player.getX(), obstacle.y - playerBounds.height);
            } else {
                player.setPosition(player.getX(), obstacle.y + obstacle.height);
            }
        }
    }

    private void updateCamera() {
        // Center camera on player
        camera.position.x = player.getX() + player.getWidth() / 2;
        camera.position.y = player.getY() + player.getHeight() / 2;

        // Ensure camera stays within map bounds
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        // Get map dimensions
        MapProperties mapProperties = map.getProperties();
        int mapWidth = mapProperties.get("width", Integer.class);
        int mapHeight = mapProperties.get("height", Integer.class);
        int tileWidth = mapProperties.get("tilewidth", Integer.class);
        int tileHeight = mapProperties.get("tileheight", Integer.class);

        float mapWidthInPixels = mapWidth * tileWidth * UNIT_SCALE;
        float mapHeightInPixels = mapHeight * tileHeight * UNIT_SCALE;

        // Keep camera within map bounds
        camera.position.x = Math.min(mapWidthInPixels - effectiveViewportWidth/2, Math.max(effectiveViewportWidth/2, camera.position.x));
        camera.position.y = Math.min(mapHeightInPixels - effectiveViewportHeight/2, Math.max(effectiveViewportHeight/2, camera.position.y));

        camera.update();
    }

    private void updateBattle(float delta) {
        // Update the quiz manager
        quizManager.update(delta);

        // Check if battle ended
        if (quizManager.isQuizCompleted()) {
            // Check if boss was defeated
            if (quizManager.isQuizPassed()) {
                // Boss defeated, go to next map
                game.progressToNextMap();
            } else {
                // Player lost, return to exploration
                inBossBattle = false;
            }
        }
    }

    private void renderWorld() {
        // Set camera for map rendering
        mapRenderer.setView(camera);

        // Begin batch for map rendering
        batch.begin();

        // Render map layers
        mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("ground"));
        mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("walls"));
        mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("decoration"));

        // End batch for map rendering
        batch.end();

        // Begin batch for sprite rendering
        batch.begin();

        // Render player
        player.render(batch);

        // Render boss
        currentBoss.render(batch);

        // End batch for sprite rendering
        batch.end();
    }

    private void renderBattle() {
        // Set up orthographic camera for UI
        camera.position.set(TechXplorerGame.WORLD_WIDTH / 2, TechXplorerGame.WORLD_HEIGHT / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Render battle background
        currentBoss.renderBattleBackground(batch);
        batch.end();

        // Render quiz UI
        quizManager.render(batch);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        player.dispose();
        currentBoss.dispose();
        quizManager.dispose();
    }
}

package com.proj.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.proj.assets.AssetDescriptors;
import com.proj.Screens.CutSceneScreen;
import com.proj.Screens.GameScreen;
import com.proj.Screens.LoadingScreen;

public class TechXplorerGame extends Game {
    // Constants
    public static final float WORLD_WIDTH = 800;
    public static final float WORLD_HEIGHT = 480;

    // Core game components
    private AssetManager assetManager;
    private SpriteBatch batch;

    private String playerName = "Player";
    private boolean isMale = true;
    // Game state
    private int currentMapIndex = 0;
    private boolean[] bossesDefeated = new boolean[5]; // Track which bosses are defeated

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        assetManager.setLoader(TiledMap.class, new TmxMapLoader());

        // Initialize the loading screen to load assets before the cutscene plays
        setScreen(new LoadingScreen(this));
    }

    // Player data methods
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerGender(boolean isMale) {
        this.isMale = isMale;
    }

    public boolean isPlayerMale() {
        return isMale;
    }

    /**
     * Start the main game after all resources are loaded
     */
    public void startGame() {
        setScreen(new CutSceneScreen(this));
    }

    /**
     * Start gameplay after cutscene is finished
     */
    public void startGameplay() {
        setScreen(new GameScreen(this));
    }

    /**
     * Move to the next map after boss is defeated
     */
    public void progressToNextMap() {
        if (currentMapIndex < 4) { // We have 5 maps (0-4)
            bossesDefeated[currentMapIndex] = true;
            currentMapIndex++;
            // Reload game screen with new map
            setScreen(new GameScreen(this));
        } else {
            // Game completed - could show an ending cutscene
            Gdx.app.log("TechXplorer", "Game completed! All bosses defeated.");
        }
    }

    /**
     * Check if the player has defeated the boss on the current map
     */
    public boolean isCurrentBossDefeated() {
        return bossesDefeated[currentMapIndex];
    }

    /**
     * Get the index of the current map
     */
    public int getCurrentMapIndex() {
        return currentMapIndex;
    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render active screen
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        getScreen().dispose();
    }

    // Getters for core game components
    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}

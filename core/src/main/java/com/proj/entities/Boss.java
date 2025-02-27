package com.proj.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Boss {
    // Constants
    protected static final int FRAME_COLS = 4;
    protected static final int FRAME_ROWS = 1;
    protected static final float FRAME_DURATION = 0.15f;

    // Common properties
    protected float x;
    protected float y;
    protected float width = 64f; // Bosses are larger than player
    protected float height = 64f;
    protected float stateTime = 0;
    protected Rectangle bounds = new Rectangle();

    // Animation
    protected Animation<TextureRegion> idleAnimation;
    protected TextureRegion currentFrame;
    protected Texture battleBackgroundTexture;

    // Boss state
    protected int health = 100;
    protected String name;
    protected String description;

    public Boss(AssetManager assetManager, float x, float y, String texturePath, String bgTexturePath, String name, String description) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.description = description;

        // Load textures
        Texture bossTexture = assetManager.get(texturePath, Texture.class);
        battleBackgroundTexture = assetManager.get(bgTexturePath, Texture.class);

        // Create animation frames
        TextureRegion[][] tmp = TextureRegion.split(bossTexture,
            bossTexture.getWidth() / FRAME_COLS,
            bossTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] idleFrames = new TextureRegion[FRAME_COLS];
        for (int i = 0; i < FRAME_COLS; i++) {
            idleFrames[i] = tmp[0][i];
        }

        idleAnimation = new Animation<>(FRAME_DURATION, idleFrames);
        currentFrame = idleFrames[0];

        // Set up collision bounds
        updateBounds();
    }

    public static Boss createBoss(int bossIndex, AssetManager assetManager, float x, float y) {
        switch (bossIndex) {
            case 0:
                return new SteelWardBoss(assetManager, x, y);
            case 1:
                return new BlazeCinderBoss(assetManager, x, y);
            case 2:
                return new MemorixBoss(assetManager, x, y);
            case 3:
                return new GlitchronBoss(assetManager, x, y);
            case 4:
                return new ExodusBoss(assetManager, x, y);
            default:
                return new SteelWardBoss(assetManager, x, y);
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        currentFrame = idleAnimation.getKeyFrame(stateTime, true);
    }

    private void updateBounds() {
        bounds.set(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, x, y, width, height);
    }

    public void renderBattleBackground(SpriteBatch batch) {
        batch.draw(battleBackgroundTexture, 0, 0, 800, 480);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void dispose() {
        // No resources to dispose as we're using the AssetManager
    }

    // Boss-specific implementations
    public static class SteelWardBoss extends Boss {
        public SteelWardBoss(AssetManager assetManager, float x, float y) {
            super(assetManager, x, y,
                "sprites/boss_steelward.png",
                "backgrounds/steelward_bg.png",
                "SteelWard",
                "A powerful PC Case robot with tough armor.");
        }
    }

    public static class BlazeCinderBoss extends Boss {
        public BlazeCinderBoss(AssetManager assetManager, float x, float y) {
            super(assetManager, x, y,
                "sprites/boss_blazecinder.png",
                "backgrounds/blazecinder_bg.png",
                "BlazeCinder",
                "A fiery cooling system robot that overheats its surroundings.");
        }
    }

    public static class MemorixBoss extends Boss {
        public MemorixBoss(AssetManager assetManager, float x, float y) {
            super(assetManager, x, y,
                "sprites/boss_memorix.png",
                "backgrounds/memorix_bg.png",
                "Memorix",
                "A devious storage device robot with incredible memory.");
        }
    }

    public static class GlitchronBoss extends Boss {
        public GlitchronBoss(AssetManager assetManager, float x, float y) {
            super(assetManager, x, y,
                "sprites/boss_glitchron.png",
                "backgrounds/glitchron_bg.png",
                "Glitchron",
                "A powerful PSU and motherboard robot that controls energy flow.");
        }
    }

    public static class ExodusBoss extends Boss {
        public ExodusBoss(AssetManager assetManager, float x, float y) {
            super(assetManager, x, y,
                "sprites/boss_exodus.png",
                "backgrounds/exodus_bg.png",
                "EXODUS",
                "The final boss, a menacing AI determined to control the world.");
        }
    }
}

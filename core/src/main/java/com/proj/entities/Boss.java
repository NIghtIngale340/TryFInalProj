package com.proj.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.proj.assets.AssetDescriptors;

public abstract class Boss {
    // Constants
    protected static final int FRAME_COLS = 4;
    protected static final int FRAME_ROWS = 1;
    protected static final float FRAME_DURATION = 0.15f;

    // Animation constants
    protected static final float FLOAT_AMPLITUDE = 5f;
    protected static final float FLOAT_SPEED = 2f;

    // Common properties
    protected float x;
    protected float y;
    protected float originalY;
    protected float width = 64f; // Bosses are larger than player
    protected float height = 64f;
    protected float stateTime = 0;
    protected Rectangle bounds = new Rectangle();
    protected boolean isSpawning = true;
    protected float spawnTime = 0;
    protected static final float SPAWN_DURATION = 1.5f;

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
        this.originalY = y;
        this.name = name;
        this.description = description;

        // Load textures
        Texture bossTexture;
        if (assetManager.isLoaded(texturePath)) {
            bossTexture = assetManager.get(texturePath, Texture.class);
        } else {
            // Fallback in case texture isn't in AssetManager
            bossTexture = new Texture(Gdx.files.internal(texturePath));
        }

        if (assetManager.isLoaded(bgTexturePath)) {
            battleBackgroundTexture = assetManager.get(bgTexturePath, Texture.class);
        } else {
            battleBackgroundTexture = new Texture(Gdx.files.internal(bgTexturePath));
        }

        // Create animation frames
        TextureRegion[][] tmp = TextureRegion.split(bossTexture,
            bossTexture.getWidth() / FRAME_COLS,
            bossTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] idleFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(tmp[0], 0, idleFrames, 0, FRAME_COLS);

        idleAnimation = new Animation<>(FRAME_DURATION, idleFrames);
        currentFrame = idleFrames[0];

        // Set up collision bounds
        updateBounds();
    }

    public static Boss createBoss(int bossIndex, AssetManager assetManager, float x, float y) {
        return switch (bossIndex) {
            case 0 -> new SteelWardBoss(assetManager, x, y);
            case 1 -> new BlazeCinderBoss(assetManager, x, y);
            case 2 -> new MemorixBoss(assetManager, x, y);
            case 3 -> new GlitchronBoss(assetManager, x, y);
            case 4 -> new ExodusBoss(assetManager, x, y);
            default -> new SteelWardBoss(assetManager, x, y);
        };
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        if (isSpawning) {
            // Handle spawn animation
            spawnTime += deltaTime;
            if (spawnTime >= SPAWN_DURATION) {
                isSpawning = false;
            }
        }

        // Apply floating effect regardless of spawn state
        float floatingY = originalY + FLOAT_AMPLITUDE * MathUtils.sin(stateTime * FLOAT_SPEED);
        y = floatingY;

        // Update animation frame
        currentFrame = idleAnimation.getKeyFrame(stateTime, true);

        // Update collision bounds
        updateBounds();
    }

    private void updateBounds() {
        bounds.set(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        Color oldColor = batch.getColor().cpy();

        if (isSpawning) {
            // Apply spawn animation visual effect
            float progress = spawnTime / SPAWN_DURATION;

            // Visual effects during spawn
            float scale = MathUtils.lerp(0.4f, 1.0f, progress);
            float alpha = MathUtils.lerp(0.3f, 1.0f, progress);

            // Set alpha for transparency effect
            batch.setColor(1, 1, 1, alpha);

            // Calculate centered position for scaling
            float scaledWidth = width * scale;
            float scaledHeight = height * scale;
            float xOffset = (width - scaledWidth) / 2;
            float yOffset = (height - scaledHeight) / 2;

            // Draw scaled with glow effect
            batch.draw(currentFrame, x + xOffset, y + yOffset, scaledWidth, scaledHeight);

            // Draw additional frame with glow effect if early in spawn
            if (progress < 0.5f) {
                batch.setColor(1, 1, 1, 0.3f * (1 - progress * 2));
                float glowScale = scale * 1.2f;
                float glowWidth = width * glowScale;
                float glowHeight = height * glowScale;
                float glowXOffset = (width - glowWidth) / 2;
                float glowYOffset = (height - glowHeight) / 2;
                batch.draw(currentFrame, x + glowXOffset, y + glowYOffset, glowWidth, glowHeight);
            }
        } else {
            // Normal rendering
            batch.draw(currentFrame, x, y, width, height);
        }

        // Restore original color
        batch.setColor(oldColor);
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

    public boolean isSpawning() {
        return isSpawning;
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

package com.proj.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    // Animation constants
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 4;
    private static final float FRAME_DURATION = 0.1f;

    // Movement constants
    private static final float MOVEMENT_SPEED = 120f; // pixels per second

    // Player state
    private float x;
    private float y;
    private float width = 32f;
    private float height = 32f;
    private float stateTime = 0;
    private Direction currentDirection = Direction.DOWN;
    private boolean isMoving = false;

    // Animation variables
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private TextureRegion currentFrame;
    private TextureRegion[] standingFrames;

    // Collision
    private Rectangle bounds = new Rectangle();

    // Enum for direction
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Player(AssetManager assetManager, float x, float y, boolean isMale) {
        this.x = x;
        this.y = y;

        // Load appropriate player sprite sheet based on gender
        Texture playerTexture;
        if (isMale) {
            playerTexture = new Texture(Gdx.files.internal("sprites/player_male.png"));
        } else {
            playerTexture = new Texture(Gdx.files.internal("sprites/player_female.png"));
        }

        TextureRegion[][] tmp = TextureRegion.split(playerTexture,
            playerTexture.getWidth() / FRAME_COLS,
            playerTexture.getHeight() / FRAME_ROWS);

        // Create animations for each direction
        TextureRegion[] walkDownFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] walkLeftFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] walkRightFrames = new TextureRegion[FRAME_COLS];
        TextureRegion[] walkUpFrames = new TextureRegion[FRAME_COLS];

        standingFrames = new TextureRegion[4]; // One standing frame per direction

        // Down animation
        for (int i = 0; i < FRAME_COLS; i++) {
            walkDownFrames[i] = tmp[0][i];
        }
        standingFrames[Direction.DOWN.ordinal()] = walkDownFrames[0];

        // Left animation
        for (int i = 0; i < FRAME_COLS; i++) {
            walkLeftFrames[i] = tmp[1][i];
        }
        standingFrames[Direction.LEFT.ordinal()] = walkLeftFrames[0];

        // Right animation
        for (int i = 0; i < FRAME_COLS; i++) {
            walkRightFrames[i] = tmp[2][i];
        }
        standingFrames[Direction.RIGHT.ordinal()] = walkRightFrames[0];

        // Up animation
        for (int i = 0; i < FRAME_COLS; i++) {
            walkUpFrames[i] = tmp[3][i];
        }
        standingFrames[Direction.UP.ordinal()] = walkUpFrames[0];

        // Initialize animations
        walkDownAnimation = new Animation<>(FRAME_DURATION, walkDownFrames);
        walkLeftAnimation = new Animation<>(FRAME_DURATION, walkLeftFrames);
        walkRightAnimation = new Animation<>(FRAME_DURATION, walkRightFrames);
        walkUpAnimation = new Animation<>(FRAME_DURATION, walkUpFrames);

        // Initialize current frame
        currentFrame = standingFrames[Direction.DOWN.ordinal()];

        // Setup collision bounds
        updateBounds();
    }

    public void update(float deltaTime, float horizontalInput, float verticalInput) {
        stateTime += deltaTime;

        // Track if player is moving
        isMoving = horizontalInput != 0 || verticalInput != 0;

        if (isMoving) {
            // Determine direction
            if (Math.abs(horizontalInput) > Math.abs(verticalInput)) {
                // Horizontal movement takes priority
                if (horizontalInput > 0) {
                    currentDirection = Direction.RIGHT;
                } else {
                    currentDirection = Direction.LEFT;
                }
            } else {
                // Vertical movement takes priority
                if (verticalInput > 0) {
                    currentDirection = Direction.UP;
                } else {
                    currentDirection = Direction.DOWN;
                }
            }

            // Update position
            x += horizontalInput * MOVEMENT_SPEED * deltaTime;
            y += verticalInput * MOVEMENT_SPEED * deltaTime;

            // Update collision bounds
            updateBounds();
        }

        // Set current animation frame
        currentFrame = getFrame();
    }

    private TextureRegion getFrame() {
        if (!isMoving) {
            return standingFrames[currentDirection.ordinal()];
        }

        // Get the appropriate animation based on direction
        Animation<TextureRegion> currentAnimation = switch (currentDirection) {
            case UP -> walkUpAnimation;
            case DOWN -> walkDownAnimation;
            case LEFT -> walkLeftAnimation;
            case RIGHT -> walkRightAnimation;
            default -> walkDownAnimation;
        };

        // Return the current frame from the animation
        return currentAnimation.getKeyFrame(stateTime, true);
    }

    private void updateBounds() {
        // Update collision rectangle to match player position
        bounds.set(x + 4, y, width - 8, height / 2);
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, x, y, width, height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    public void dispose() {
        // No resources to dispose as we're using the AssetManager
    }
}

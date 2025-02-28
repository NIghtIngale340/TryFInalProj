package com.proj.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MapInteractionHandler {
    private mapManager mapManager;
    private Array<Rectangle> collisionObjects;

    public MapInteractionHandler(mapManager mapManager) {
        this.mapManager = mapManager;
        this.collisionObjects = mapManager.getCollisionObjects();
    }

    public boolean isCollidingWithMap(Rectangle entityBounds) {
        for (Rectangle collisionRect : collisionObjects) {
            if (entityBounds.overlaps(collisionRect)) {
                return true;
            }
        }
        return false;
    }

    public void updateCollisionObjects() {
        // Call this when map changes
        this.collisionObjects = mapManager.getCollisionObjects();
    }

    public Vector2 adjustPositionForCollision(Vector2 currentPosition, Vector2 targetPosition, float width, float height) {
        // Create a rectangle for the target position
        Rectangle targetBounds = new Rectangle(
            targetPosition.x - width/2,
            targetPosition.y - height/2,
            width, height
        );

        // Check if the target position collides with any obstacles
        if (isCollidingWithMap(targetBounds)) {
            return currentPosition;  // Cannot move there, return original position
        }

        return targetPosition;  // Safe to move
    }
}

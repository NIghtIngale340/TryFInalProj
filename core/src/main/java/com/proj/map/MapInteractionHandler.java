package com.proj.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MapInteractionHandler {

    private Array<Rectangle> collisionObjects;

    public MapInteractionHandler(Array<Rectangle> collisionObjects) {
       this.collisionObjects = collisionObjects;
    }

    public boolean isCollidingWithMap(Rectangle entityBounds) {
        for (Rectangle collisionRect : collisionObjects) {
            if (entityBounds.overlaps(collisionRect)) {
                return true;
            }
        }
        return false;
    }

    public void updateCollisionObjects(Array<Rectangle> newcollisionObjects) {
        // Call this when map changes
        this.collisionObjects.clear();
        this.collisionObjects.addAll(newcollisionObjects);
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

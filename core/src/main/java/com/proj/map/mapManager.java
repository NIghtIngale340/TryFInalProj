package com.proj.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.proj.assets.AssetDescriptors;

public class mapManager implements Disposable {
    private AssetManager assetManager;
    private TiledMap currentMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int currentMapIndex;

    // Common layer names across maps
    private static final String[] FLOOR_LAYER_NAMES = {"Floor", "Tile Layer 1"};
    private static final String[] WALL_LAYER_NAMES = {"Walls", "Wall", "WALL"};
    private static final String[] COLLISION_LAYER_NAMES = {"Collisions", "collisions"};
    private static final String[] COLLISION_OBJECT_NAMES = {"collision"};
    private static final String[] SPAWN_POINT_NAMES = {"starting position", "starting point"};
    private static final String[] BOSS_SPAWN_NAMES = {"boss spawn point"};

    public mapManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.currentMapIndex = -1;
    }

    public void loadMap(int mapIndex) {
        if (mapIndex < 0 || mapIndex > 4) {
            throw new IllegalArgumentException("Map index must be between 0 and 4");
        }

        // Dispose of the current map renderer if it exists
        if (mapRenderer != null) {
            mapRenderer.dispose();
        }

        // Load the new map
        TiledMap map = null;
        switch (mapIndex) {
            case 0:
                map = assetManager.get(AssetDescriptors.MAP_0);
                break;
            case 1:
                map = assetManager.get(AssetDescriptors.MAP_1);
                break;
            case 2:
                map = assetManager.get(AssetDescriptors.MAP_2);
                break;
            case 3:
                map = assetManager.get(AssetDescriptors.MAP_3);
                break;
            case 4:
                map = assetManager.get(AssetDescriptors.MAP_4);
                break;
        }

        this.currentMap = map;
        this.currentMapIndex = mapIndex;
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
    }

    public TiledMapTileLayer getFloorLayer() {
        return findLayerByNames(FLOOR_LAYER_NAMES);
    }

    public TiledMapTileLayer getWallLayer() {
        return findLayerByNames(WALL_LAYER_NAMES);
    }

    public TiledMapTileLayer getCollisionLayer() {
        return findLayerByNames(COLLISION_LAYER_NAMES);
    }

    public Array<Rectangle> getCollisionObjects() {
        Array<Rectangle> collisionRects = new Array<>();

        // First try to get collision objects from object layers
        for (String name : COLLISION_OBJECT_NAMES) {
            MapLayer objectLayer = currentMap.getLayers().get(name);
            if (objectLayer != null) {
                MapObjects objects = objectLayer.getObjects();
                for (MapObject object : objects) {
                    if (object instanceof RectangleMapObject) {
                        RectangleMapObject rectObject = (RectangleMapObject) object;
                        collisionRects.add(rectObject.getRectangle());
                    }
                }
            }
        }

        // If no collision objects were found, try to get them from collision layers
        if (collisionRects.size == 0) {
            TiledMapTileLayer collisionLayer = getCollisionLayer();
            if (collisionLayer != null) {
                // Convert collision tiles to rectangles
                for (int x = 0; x < collisionLayer.getWidth(); x++) {
                    for (int y = 0; y < collisionLayer.getHeight(); y++) {
                        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                        if (cell != null && cell.getTile() != null) {
                            collisionRects.add(new Rectangle(x * collisionLayer.getTileWidth(),
                                y * collisionLayer.getTileHeight(),
                                collisionLayer.getTileWidth(),
                                collisionLayer.getTileHeight()));
                        }
                    }
                }
            }
        }

        return collisionRects;
    }

    public Vector2 getPlayerSpawnPosition() {
        for (String name : SPAWN_POINT_NAMES) {
            MapLayer layer = currentMap.getLayers().get(name);
            if (layer != null && layer.getObjects().getCount() > 0) {
                MapObject object = layer.getObjects().get(0);
                if (object != null) {
                    float x = object.getProperties().get("x", Float.class);
                    float y = object.getProperties().get("y", Float.class);
                    return new Vector2(x, y);
                }
            }
        }

        // Default spawn position if none found
        return new Vector2(100, 100);
    }

    public Vector2 getBossSpawnPosition() {
        for (String name : BOSS_SPAWN_NAMES) {
            MapLayer layer = currentMap.getLayers().get(name);
            if (layer != null && layer.getObjects().getCount() > 0) {
                MapObject object = layer.getObjects().get(0);
                if (object != null) {
                    float x = object.getProperties().get("x", Float.class);
                    float y = object.getProperties().get("y", Float.class);
                    return new Vector2(x, y);
                }
            }
        }

        // Default boss position if none found
        return new Vector2(200, 200);
    }

    public void render(OrthographicCamera camera) {
        if (mapRenderer != null) {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
    }

    public int getCurrentMapIndex() {
        return currentMapIndex;
    }

    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public int getMapWidth() {
        if (currentMap == null) return 0;

        MapProperties properties = currentMap.getProperties();
        int width = properties.get("width", Integer.class);
        int tileWidth = properties.get("tilewidth", Integer.class);
        return width * tileWidth;
    }

    public int getMapHeight() {
        if (currentMap == null) return 0;

        MapProperties properties = currentMap.getProperties();
        int height = properties.get("height", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        return height * tileHeight;
    }

    private TiledMapTileLayer findLayerByNames(String[] names) {
        if (currentMap == null) return null;

        for (String name : names) {
            TiledMapTileLayer layer = (TiledMapTileLayer) currentMap.getLayers().get(name);
            if (layer != null) {
                return layer;
            }
        }

        return null;
    }

    @Override
    public void dispose() {
        if (mapRenderer != null) {
            mapRenderer.dispose();
            mapRenderer = null;
        }
        // Note: We don't dispose the TiledMap as it's managed by the AssetManager
    }
}

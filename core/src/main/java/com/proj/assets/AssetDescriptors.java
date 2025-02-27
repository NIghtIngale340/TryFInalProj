package com.proj.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    // Maps
    public static final AssetDescriptor<TiledMap> MAP_0 = new AssetDescriptor<>("maps/map0.tmx", TiledMap.class);
    public static final AssetDescriptor<TiledMap> MAP_1 = new AssetDescriptor<>("maps/map1.tmx", TiledMap.class);
    public static final AssetDescriptor<TiledMap> MAP_2 = new AssetDescriptor<>("maps/map2.tmx", TiledMap.class);
    public static final AssetDescriptor<TiledMap> MAP_3 = new AssetDescriptor<>("maps/map3.tmx", TiledMap.class);
    public static final AssetDescriptor<TiledMap> MAP_4 = new AssetDescriptor<>("maps/map4.tmx", TiledMap.class);

    // Player
    public static final AssetDescriptor<Texture> PLAYER = new AssetDescriptor<>("sprites/player.png", Texture.class);

    // Bosses
    public static final AssetDescriptor<Texture> BOSS_STEELWARD = new AssetDescriptor<>("sprites/boss_steelward.png", Texture.class);
}

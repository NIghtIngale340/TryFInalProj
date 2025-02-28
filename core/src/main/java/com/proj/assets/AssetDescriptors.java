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
    public static final AssetDescriptor<Texture> BOSS_BLAZECINDER = new AssetDescriptor<>("sprites/boss_blazecinder.png", Texture.class);
    public static final AssetDescriptor<Texture> BOSS_MEMORIX = new AssetDescriptor<>("sprites/boss_memorix.png", Texture.class);
    public static final AssetDescriptor<Texture> BOSS_GLITCHRON = new AssetDescriptor<>("sprites/boss_glitchron.png", Texture.class);
    public static final AssetDescriptor<Texture> BOSS_EXODUS = new AssetDescriptor<>("sprites/boss_exodus.png", Texture.class);

    // Boss backgrounds
    public static final AssetDescriptor<Texture> BG_STEELWARD = new AssetDescriptor<>("backgrounds/steelward_bg.png", Texture.class);
    public static final AssetDescriptor<Texture> BG_BLAZECINDER = new AssetDescriptor<>("backgrounds/blazecinder_bg.png", Texture.class);
    public static final AssetDescriptor<Texture> BG_MEMORIX = new AssetDescriptor<>("backgrounds/memorix_bg.png", Texture.class);
    public static final AssetDescriptor<Texture> BG_GLITCHRON = new AssetDescriptor<>("backgrounds/glitchron_bg.png", Texture.class);
    public static final AssetDescriptor<Texture> BG_EXODUS = new AssetDescriptor<>("backgrounds/exodus_bg.png", Texture.class);

    // Cutscene
    public static final AssetDescriptor<Texture> CUTSCENE_INTRO = new AssetDescriptor<>("cutscene/intro.png", Texture.class);

    // UI
    public static final AssetDescriptor<Skin> UI_SKIN = new AssetDescriptor<>("ui/ui_skin.json", Skin.class);
}

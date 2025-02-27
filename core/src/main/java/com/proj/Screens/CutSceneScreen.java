package com.proj.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.proj.core.TechXplorerGame;

public class CutSceneScreen extends ScreenAdapter {
    private final TechXplorerGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private BitmapFont font;

    private Array<String> dialogues;
    private int currentDialogue = 0;

    private float timeElapsed = 0f;
    private Texture introTexture;
    private boolean cutsceneFinished = false;

    public CutSceneScreen(TechXplorerGame game) {
        this.game = game;
        this.batch = game.getBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT, camera);

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        // Initialize dialogue text
        dialogues = new Array<>();
        dialogues.add("In the year 21XX, robots have taken over the world...");
        dialogues.add("DemoniTech created a powerful anti-AI robot named CPN...");
        dialogues.add("But EXODUS attacked the lab, destroyed CPN, and scattered its parts...");
        dialogues.add("Dr. Fox's child must now recover CPN's lost pieces...");
        dialogues.add("Battle robot bosses and stop EXODUS from taking over the world!");

        // Load cutscene texture
        introTexture = game.getAssetManager().get("cutscene/intro.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update time
        timeElapsed += delta;

        // Process input (skip cutscene)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            nextDialogue();
        }

        // Auto advance dialogue every 5 seconds
        if (timeElapsed > 5f) {
            timeElapsed = 0f;
            nextDialogue();
        }

        // Check if cutscene is finished
        if (cutsceneFinished) {
            game.startGameplay();
            return;
        }

        // Render cutscene
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background image
        batch.draw(introTexture, 0, 0, TechXplorerGame.WORLD_WIDTH, TechXplorerGame.WORLD_HEIGHT);

        // Draw current dialogue text
        if (currentDialogue < dialogues.size) {
            font.draw(batch, dialogues.get(currentDialogue), 50, 100);
            font.draw(batch, "Press SPACE to continue", 50, 50);
        }

        batch.end();
    }

    private void nextDialogue() {
        currentDialogue++;
        if (currentDialogue >= dialogues.size) {
            cutsceneFinished = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}

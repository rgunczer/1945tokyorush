package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class Screen {

    SpriteBatch batch = new SpriteBatch();

    protected void beginRender() {
        batch.setProjectionMatrix(TokyoRushGame.camera.combined);
        batch.begin();
    }

    protected void endRender() {
        batch.end();
        batch.flush();
    }

    public abstract void create();
    public abstract void init();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void touchDown(Vector3 position);
    public abstract void touchMove(Vector3 position);

}

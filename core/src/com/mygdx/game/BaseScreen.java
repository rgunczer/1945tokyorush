package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen implements Screen {

    protected Stage mainStage;
    protected Stage uiStage;

    SpriteBatch batch = new SpriteBatch();

    public BaseScreen()
    {
        mainStage = new Stage();
        uiStage = new Stage();

        init();
    }

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
    public abstract void update(float dt);

//    public void render(float dt)
//    {
//        uiStage.act(dt);
//        mainStage.act(dt);
//
//        update(dt);
//
//        Gdx.gl.glClearColor(75f/255f, 94f/255f, 15f/255f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        mainStage.draw();
//        uiStage.draw();
//    }

    public abstract void touchDown(Vector3 position);
    public abstract void touchMove(Vector3 position);

    public void dispose() { }
    public void hide() { }
    public void resume() { }
    public void pause() { }
    public void resize(int width, int height) { }
    public void show() { }
}

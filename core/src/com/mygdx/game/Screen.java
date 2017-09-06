package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class Screen {

    public static TokyoRushGame game;

    public abstract void render(SpriteBatch batch);
    public abstract void touchDown(Vector3 position);
}

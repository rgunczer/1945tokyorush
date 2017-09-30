package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Airplane {

    float rot;
    float scale;
    public Vector2 pos = new Vector2();
    public Vector2 vel = new Vector2();
    public TextureRegion body;
    public TextureRegion shadow;

    public void init() {

    }

    public void update(float delta, float scrollY) {
        pos.y += scrollY;

        pos.x += vel.x * delta;
        pos.y += vel.y * delta;
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        float w = body.getRegionWidth() * TokyoRushGame.scale;
        float h = body.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;
        float shadowOffset = 6f * TokyoRushGame.scale;

        batch.draw(shadow, x + offset.x + shadowOffset, y + offset.y - shadowOffset, originX, originY, w, h, scaleX, scaleY, rot);
        batch.draw(body, x + offset.x, y + offset.y, originX, originY, w, h, scale, scale, rot);

    }
}

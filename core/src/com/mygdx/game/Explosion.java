package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Explosion {

    private static Texture texture;
    private float startTime;
    private static Animation<TextureRegion> anim;

    public Vector2 pos = new Vector2();
    public boolean live;
    public float scale = 0.9f;
    float rot = 0f;

    public static void create() {
        texture = TextureFactory.create("explosion.png");
        TextureRegion[][] regions = TextureRegion.split(texture, 128, 128);

        int index = 0;
        TextureRegion[] frames = new TextureRegion[16];
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                frames[index++] = regions[i][j];
            }
        }
        anim = new Animation<TextureRegion>(0.2f, frames);
    }

    public void init(Vector2 pos) {
        startTime = 0f;
        live = true;
        this.pos.set(pos);
        this.rot = MathUtils.random(0f, 360f);
    }

    public void update(float delta, float scrollY) {
        pos.y += scrollY;
        startTime += 0.1f;
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        boolean finished = anim.isAnimationFinished(startTime);
        if (!finished) {
            TextureRegion currentFrame = anim.getKeyFrame(startTime, false);

            float w = currentFrame.getRegionWidth() * TokyoRushGame.scale;
            float h = currentFrame.getRegionHeight() * TokyoRushGame.scale;
            float originX = w * 0.5f;
            float originY = h * 0.5f;

            float x = pos.x - w * 0.5f;
            float y = pos.y - h * 0.5f;

            batch.draw(currentFrame, x + offset.x, y + offset.y, originX, originY, w, h, scale, scale, rot);
        } else {
            live = false;
        }
    }
}

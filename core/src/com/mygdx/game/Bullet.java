package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    public static Texture bulletTexture;
    public static TextureRegion playerBullet;
    public static TextureRegion redBullet;
    public static TextureRegion yellowBullet;
    public static TextureRegion blueBullet;

    public boolean live;

    public Vector2 pos;
    public Vector2 vel;
    public int hitPoint;
    public float rot;
    TextureRegion current;

    public Bullet() {
        live = false;
        pos = new Vector2();
        vel = new Vector2();
        rot = 0f;
    }

    public static void create() {
        bulletTexture = TextureFactory.create("bullets.png");
        playerBullet = new TextureRegion(bulletTexture, 0, 0, 64, 64);
        redBullet = new TextureRegion(bulletTexture, 64, 0, 64, 64);
        yellowBullet = new TextureRegion(bulletTexture, 128, 0, 64, 64);
        blueBullet = new TextureRegion(bulletTexture, 196, 0, 64, 64);
    }

    public void set(TextureRegion type,
                    float x, float y,
                    float vx, float vy) {
        live = true;
        current = type;
        pos.set(x, y);
        vel.set(vx, vy);
        this.rot = vel.angle() + 90f;
        this.hitPoint = 2;
    }

    public void update(float delta, float scrollY) {
        pos.y += scrollY;

        pos.x += vel.x * delta;
        pos.y += vel.y * delta;

        OrthographicCamera camera = TokyoRushGame.camera;

        if (pos.x > camera.viewportWidth) {
            live = false;
        }

        if (pos.x < 0f) {
            live = false;
        }

        if (pos.y > camera.viewportHeight) {
            live = false;
        }

        if (pos.y < 0f) {
            live = false;
        }
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        float w = playerBullet.getRegionWidth() * TokyoRushGame.scale;
        float h = playerBullet.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;
        float scaleX = 1.0f;
        float scaleY = 1.0f;

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;

        batch.draw(current, x, y, originX, originY, w, h, scaleX, scaleY, rot);
    }

}

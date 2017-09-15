package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Tank {

    public enum TankState {
        IDLE,
        MOVING,
        SEEK,
        TAKING_DAMAGE,
        DESTROYED
    }

    public static int TANK_TYPE_SMALL = 0;
    public static int TANK_TYPE_BIG = 1;

    TextureRegion body;
    TextureRegion shadow;

    float rot;
    Vector2 pos;
    Vector2 vel;
    float scale;

    public TankState state;

    int type;

    public Turret turret;

    public Tank(int type, TextureRegion body, TextureRegion shadow, float scale) {
        this.type = type;
        this.body = body;
        this.shadow = shadow;
        this.scale = scale;
    }

    public Tank(Tank other) {
        this.type = other.type;
        this.body = other.body;
        this.shadow = other.shadow;
        this.pos = new Vector2();
        this.scale = other.scale;

        this.turret = new Turret(other.turret);
    }

    public void create() {
        pos = new Vector2();
    }

    public void init() {
        state = TankState.IDLE;
    }

    public void update(float delta, float scrollY) {
        pos.y += scrollY;

        pos.x += vel.x * delta;
        pos.y += vel.y * delta;

        if (turret != null) {
            turret.update(delta, scrollY);
        }
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        float w = body.getRegionWidth() * TokyoRushGame.scale;
        float h = body.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;
        float scaleX = scale;
        float scaleY = scale;

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;
        float shadowOffset = 6f * TokyoRushGame.scale;

        batch.draw(shadow, x + offset.x + shadowOffset, y + offset.y - shadowOffset, originX, originY, w, h, scaleX, scaleY, rot);
        batch.draw(body, x + offset.x, y + offset.y, originX, originY, w, h, scaleX, scaleY, rot);

        if (turret != null) {
            turret.draw(batch, new Vector2(pos.x, pos.y), offset);
        }

    }

}

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

    public TankState state;

    int type;

    public Turret turret;

    public Tank(int type, TextureRegion body, TextureRegion shadow) {
        this.type = type;
        this.body = body;
        this.shadow = shadow;
    }


    public void create() {
        turret = new Turret();
        pos = new Vector2();
    }

    public void init() {
        state = TankState.IDLE;
    }

    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {
        float w = body.getRegionWidth() * TokyoRushGame.scale;
        float h = body.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;
        float scaleX = 1.0f;
        float scaleY = 1.0f;

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;

        batch.draw(body, x, y, originX, originY, w, h, scaleX, scaleY, rot);
    }

}

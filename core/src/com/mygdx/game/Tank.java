package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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


    public TankState state;

    public Turret turret;

    public void create() {
        turret = new Turret();

    }

    public void init() {
        state = TankState.IDLE;
    }

    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {

    }

}

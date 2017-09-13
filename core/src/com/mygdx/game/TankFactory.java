package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import org.w3c.dom.Text;

public class TankFactory {


    public static int MAX_TANK_TYPES = 2;

    public Texture texture;
    public TextureRegion body;
    public TextureRegion shadow;
    public TextureRegion hit;
    public TextureRegion damaged;
    public TextureRegion track;


    Array<Tank> tanks;

    public void create() {
        tanks = new Array<Tank>(MAX_TANK_TYPES);
        texture = new Texture("tansks.png");

        TextureRegion body;
        TextureRegion shadow;
        TextureRegion hit;
        TextureRegion damaged;
        TextureRegion track;

        body = new TextureRegion(texture, 0, 0, 128, 128);
        shadow = new TextureRegion(texture, 0, 128, 128, 128);
        tanks.add(new Tank(Tank.TANK_TYPE_SMALL, body, shadow));


    }

    public Tank get(int type) {
        return tanks.get(type);
    }

}

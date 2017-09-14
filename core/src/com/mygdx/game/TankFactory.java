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
        texture = new Texture("land_units.png");

        TextureRegion body;
        TextureRegion shadow;
        TextureRegion hit;
        TextureRegion damaged;
        TextureRegion track;

        int sc = 2;

//        "shadow"=(0,0,50,64)
//
//        "body"=(50,0,50,64)
//        "body_hit"=(50,135,50,64)
//
//        "wreck"=(50,64,50,64)
//        "wreck_shadow"=(0,66,50,64)
//
//        "turret"=(114,10,21,61)
//        "turret_wreck"=(114,76,21,61)
//        "turret_hit"=(114,149,21,61)
//
//        "track"=(50,214,50,28)


        body = new TextureRegion(texture, 50*sc, 0, 50*sc, 64*sc);
        shadow = new TextureRegion(texture, 0, 0, 50*sc, 64*sc);
        tanks.add(new Tank(Tank.TANK_TYPE_SMALL, body, shadow));


    }

    public Tank get(int type) {
        return tanks.get(type);
    }

}

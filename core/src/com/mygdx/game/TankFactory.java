package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


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
        int sc = 2;

        tanks = new Array<Tank>(MAX_TANK_TYPES);
        texture = new Texture("land_units.png");

        TextureRegion body;
        TextureRegion shadow;
        TextureRegion hit;
        TextureRegion damaged;
        TextureRegion track;


        TextureRegion turretNormal;
        TextureRegion turretWreck;
        TextureRegion turretHit;





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

        Tank tank;
        Turret turret;

        float scale = 0.6f;
        body = new TextureRegion(texture, 50*sc, 0, 50*sc, 64*sc);
        shadow = new TextureRegion(texture, 0, 0, 50*sc, 64*sc);
        tank = new Tank(Tank.TANK_TYPE_SMALL, body, shadow, scale);


        turretNormal = new TextureRegion(texture, 114*sc, 10*sc, 21*sc, 61*sc);
        turretWreck = new TextureRegion(texture, 114*sc, 76*sc, 21*sc, 61*sc);
        turretHit = new TextureRegion(texture, 114*sc, 149*sc, 21*sc, 61*sc);

        turret = new Turret(new Vector2(1f, 1f), turretNormal, turretHit);
        turret.scale = scale;
        tank.turret = turret;
        tanks.add(tank);



    }

    public Tank get(int type) {
        return tanks.get(type);
    }

}

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TankFactory {

    public static int MAX_TANK_TYPES = 2;
    public static int MAX_TURRET_TYPES = 2;

    public Texture texture;

    Array<TankTemplate> tankTemplates;
    Array<TurretTemplate> turretTemplates;

    public void create() {
        final int textureScaleFactor = 2;

        tankTemplates = new Array<TankTemplate>(MAX_TANK_TYPES);
        turretTemplates = new Array<TurretTemplate>(MAX_TURRET_TYPES);

        texture = new Texture("land_units.png");

        TankTemplate tankTemplate;
        TurretTemplate turretTemplate;

        tankTemplate = createTankTemplateSmall(textureScaleFactor);
        turretTemplate = createTankTurretSmall(textureScaleFactor);

        tankTemplates.add(tankTemplate);
        turretTemplates.add(turretTemplate);
    }

    private TurretTemplate createTankTurretSmall(final int textureScaleFactor) {
        final int sc = textureScaleFactor;
        final float scale = 0.6f;

        TextureRegion turretNormal = new TextureRegion(texture, 114*sc, 10*sc, 21*sc, 61*sc);
        TextureRegion turretWreck = new TextureRegion(texture, 114*sc, 76*sc, 21*sc, 61*sc);
        TextureRegion turretHit = new TextureRegion(texture, 114*sc, 149*sc, 21*sc, 61*sc);

        TurretTemplate turretTemplate = new TurretTemplate(
                Turret.TURRET_TANK_SMALL,
                new Vector2(1f, 1f),
                turretNormal,
                turretHit,
                turretWreck,
                scale);

        return turretTemplate;
    }

    private TankTemplate createTankTemplateSmall(final int textureScaleFactor) {
        final int sc = textureScaleFactor;
        final float scale = 0.6f;
        final TextureRegion bodyNormal = new TextureRegion(texture, 50*sc, 0, 50*sc, 64*sc);
        final TextureRegion bodyShadow = new TextureRegion(texture, 0, 0, 50*sc, 64*sc);
        final TextureRegion bodyHit = new TextureRegion(texture, 50*sc, 135*sc, 50*sc, 64*sc);
        final TextureRegion bodyWreck = new TextureRegion(texture, 50*sc, 64*sc, 50*sc, 64*sc);
        final TextureRegion bodyWreckShadow = new TextureRegion(texture, 0, 66*sc, 50*sc, 64*sc);
        final TextureRegion track = new TextureRegion(texture, 50*sc, 214*sc, 50*sc, 28*sc);
        final float boundingCircleRadius = 34f * TokyoRushGame.scale;
        final int hitPoint = 10;
        TankTemplate tankTemplate = new TankTemplate(
                Tank.TANK_TYPE_SMALL,
                bodyNormal,
                bodyShadow,
                bodyHit,
                bodyWreck,
                bodyWreckShadow,
                track,
                scale,
                boundingCircleRadius,
                hitPoint);

        return tankTemplate;
    }


    Tank get(int type) {
        TankTemplate tankTemplate = null;
        TurretTemplate turretTemplate = null;

        if (type == Tank.TANK_TYPE_SMALL) {
            tankTemplate = tankTemplates.get(type);
            turretTemplate = turretTemplates.get(Turret.TURRET_TANK_SMALL);
        } else if (type == Tank.TANK_TYPE_BIG) {

        }

        if (tankTemplate != null && turretTemplate != null) {
            Tank tank = new Tank(tankTemplate);
            Turret turret = new Turret(turretTemplate);
            tank.addTurret(turret);
            return tank;
        }

        return null;
    }

}

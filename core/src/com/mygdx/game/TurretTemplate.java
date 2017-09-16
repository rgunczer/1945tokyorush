package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TurretTemplate {

    TextureRegion turretNormal;
    TextureRegion turretHit;
    TextureRegion turretWreck;

    int type;
    Vector2 parentOffset;
    float scale;

    TurretTemplate(int type,
                    Vector2 parentOffset,
                    TextureRegion turretNormal,
                    TextureRegion turretHit,
                    TextureRegion turretWreck,
                   float scale) {
        this.type = type;
        this.parentOffset = parentOffset;
        this.turretNormal = turretNormal;
        this.turretHit = turretHit;
        this.turretWreck = turretWreck;
        this.scale = scale;
    }

}

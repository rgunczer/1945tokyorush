package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class TankTemplate {

    public TextureRegion bodyNormal;
    public TextureRegion bodyShadow;
    public TextureRegion bodyHit;
    public TextureRegion bodyWreck;
    public TextureRegion bodyWreckShadow;
    public TextureRegion track;

    public int type;
    public float scale;
    public int hitPoint;

    public Circle boundingCircle;

    public TankTemplate(int type,
                        TextureRegion bodyNormal,
                        TextureRegion bodyShadow,
                        TextureRegion bodyHit,
                        TextureRegion bodyWreck,
                        TextureRegion bodyWreckShadow,
                        TextureRegion track,
                        float scale,
                        float boundingCircleRadius,
                        int hitPoint) {
        this.bodyNormal = bodyNormal;
        this.bodyShadow = bodyShadow;
        this.bodyHit = bodyHit;
        this.bodyWreck = bodyWreck;
        this.bodyWreckShadow = bodyWreckShadow;
        this.track = track;

        this.type = type;
        this.scale = scale;

        this.boundingCircle = new Circle(new Vector2(0f, 0f), boundingCircleRadius);
        this.hitPoint = hitPoint;
    }

}

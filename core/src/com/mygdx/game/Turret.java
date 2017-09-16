package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Turret {

    public static int TURRET_TANK_SMALL = 0;
    public static int TURRET_TANK_BIG = 1;

    TextureRegion body;
    float scale;
    float rot;
    Vector2 offsetParent;
    TurretTemplate template;

    public Turret(TurretTemplate template) {
        this.template = template;
        this.offsetParent = template.parentOffset;
        this.body = template.turretNormal;
        this.scale = template.scale;
    }

    public void showHit() {
        this.body = template.turretHit;
    }

    public void showNormal() {
        this.body = template.turretNormal;
    }

    public void showWreck() {
        this.body = template.turretWreck;
    }

    public void update(float delta, float scrollY) {
        rot-=0.3f;
    }

    public void draw(SpriteBatch batch, Vector2 parentPos, Vector2 offset) {
        float w = body.getRegionWidth() * TokyoRushGame.scale;
        float h = body.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;
        float scaleX = scale;
        float scaleY = scale;

        Vector2 pos = new Vector2(parentPos.x + offsetParent.x, parentPos.y + offsetParent.y);

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;

        batch.draw(body, x + offset.x, y + offset.y, originX, originY, w, h, scaleX, scaleY, rot);
    }

}

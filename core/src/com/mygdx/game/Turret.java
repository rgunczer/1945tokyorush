package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Turret {

    TextureRegion body;
    TextureRegion hit;
    float scale;
    float rot;
    Vector2 offsetParent;

    public Turret(Vector2 offsetParent, TextureRegion body, TextureRegion hit) {
        this.offsetParent = offsetParent;
        this.body = body;
        this.hit = hit;
    }

    public Turret(Turret other) {
        this.offsetParent = other.offsetParent;
        this.body = other.body;
        this.hit = other.hit;
        this.scale = other.scale;
    }

    public void update(float delta, float scrollY) {
        rot-=0.1f;
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

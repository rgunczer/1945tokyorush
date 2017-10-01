package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Airplane {

    public static int ZERO_PANTHER = 0;
    public static int ZERO_BLACK = 1;
    public static int ZERO_WHITE = 2;
    public static int ZERO_GREEN = 3;


    float propAnimStartTime;
    float rot;
    float scale;
    float speed;
    public Vector2 pos = new Vector2();
    public Vector2 vel = new Vector2();
    public TextureRegion body;
    public TextureRegion shadow;
    AirplaneTemplate template;

    public Airplane(AirplaneTemplate template) {
        this.template = template;
        body = template.body;
        shadow = template.shadow;
        scale = 0.68f;
        rot = 180f;
    }

    private Vector2 getRandomVelocity(float rot) {
        Vector2 vel = new Vector2(0f, 30f);
        vel.rotate( rot );

        return vel;
    }

    public void init(float speed) {
        propAnimStartTime = 0f;
        this.speed = speed;
        this.rot = MathUtils.random(0f, 360f);
        this.vel = getRandomVelocity(rot);
        this.vel.nor();
        this.vel.scl(this.speed);
        this.body = template.body;
    }

    public void update(float delta, float scrollY) {
        propAnimStartTime += 0.1f;
        pos.y += scrollY;

        pos.x += vel.x * delta;
        pos.y += vel.y * delta;
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        float w = body.getRegionWidth() * TokyoRushGame.scale;
        float h = body.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;
        float shadowOffset = 60f * TokyoRushGame.scale;

        float scaleShadow = scale * 0.6f;

        batch.draw(shadow, x + offset.x + shadowOffset, y + offset.y - shadowOffset, originX, originY, w, h, scaleShadow, scaleShadow, rot);
        batch.draw(body, x + offset.x, y + offset.y, originX, originY, w, h, scale, scale, rot);

        TextureRegion prop = AirplaneTemplate.propellerAnim.getKeyFrame(propAnimStartTime, true);
        batch.draw(prop, x, y, originX, originY, w, h, scale, scale, rot);

    }
}

package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class Player {

    public enum PlayerStateEnum {
        IDLE_ON_AIRFIELD,
        FLYING,
        TAKEOFF,
    }

    Texture texture;
    TextureRegion hit;
    TextureRegion normal;
    TextureRegion shadow;

    float takeOffSpeed = 100f; ///400f;

    float distanceToTarget;

    float fingerOffsetY;

    Animation propellerAnim;

    Vector2 posTakeOff = new Vector2();

    Vector2 target = new Vector2();
    Vector2 diff = new Vector2();
    Vector2 pos = new Vector2();
    Vector2 posTarget = new Vector2();
    Vector2 velocity = new Vector2();

    float speed;

    public float shadowDistance;
    public float scaleShadow;

    PlayerStateEnum state;

    public float scale = 1f;


    public void create() {
        state = PlayerStateEnum.IDLE_ON_AIRFIELD;
        final String textureName = "player-and-propeller.png";

        texture = new Texture(textureName);
        hit = new TextureRegion(texture, 0, 0, 168, 168);
        normal = new TextureRegion(texture, 168, 0, 168, 168);
        shadow = new TextureRegion(texture, 336, 0, 168, 168);

        TextureRegion prop0 = new TextureRegion(texture, 0, 168, 168, 84);
        TextureRegion prop1 = new TextureRegion(texture, 168, 168, 168, 84);
        TextureRegion prop2 = new TextureRegion(texture, 336, 168, 168, 84);

        propellerAnim = new Animation(1f, prop0, prop1, prop2);
        propellerAnim.setPlayMode(Animation.PlayMode.LOOP);

        OrthographicCamera camera = TokyoRushGame.camera;

        fingerOffsetY = camera.viewportWidth / 6f;

        float w = normal.getRegionWidth();
        float h = normal.getRegionHeight();
        float x = camera.viewportWidth * 0.5f;
        float y = camera.viewportWidth / 4.0f;

        pos.x = x;
        pos.y = y;

        posTakeOff.x = pos.x;
        posTakeOff.y = pos.y;
    }

    public void setOnAirfield() {
        pos.set(posTakeOff);

        takeOffSpeed = 400f;
        scale = 0.75f;
        shadowDistance = 5f * TokyoRushGame.scale;
        scaleShadow = scale * 0.95f;
        state = PlayerStateEnum.IDLE_ON_AIRFIELD;
    }

    public void setOnLevel() {
        state = PlayerStateEnum.FLYING;
        OrthographicCamera camera = TokyoRushGame.camera;
        pos.x = camera.viewportWidth * 0.5f;
        pos.y = camera.viewportWidth / 3.0f;

        posTarget.set(pos);

        speed = 20.0f * TokyoRushGame.scale;

        scale = 1.0f;
        shadowDistance = 60f * TokyoRushGame.scale;
        scaleShadow = 0.5f;
    }

    public void setTargetPosition(float x, float y) {
        target.set(x, y + fingerOffsetY);

        diff.x = target.x - pos.x;
        diff.y = target.y - pos.y;

        float toTarget = target.dst(pos) * 1.01f;

        float len = diff.len();
        if (len > speed * TokyoRushGame.scale) {
            diff.nor();
            velocity.set(diff);
            velocity.scl(speed);
            posTarget.set(target);
            distanceToTarget = toTarget;
        }
    }

    public void update(float delta) {
        switch (state) {
            case TAKEOFF:
                pos.y += takeOffSpeed * delta;
                takeOffSpeed += 10f;

                if (pos.y > TokyoRushGame.camera.viewportHeight / 3f) {
                    shadowDistance += 30f * delta;
                    scaleShadow *= 0.995f;
                }

                break;

            case FLYING: {
                float distance = posTarget.dst(pos);
                System.out.println("distance: " + distance);
                if (distance < distanceToTarget) {
                    float x = velocity.x * speed * delta;
                    float y = velocity.y * speed * delta;
                    pos.add(x, y);
                    distanceToTarget = distance;
                }
            }
            break;
        }
    }

    public void render(SpriteBatch batch) {
//        public void draw (TextureRegion region,
//        float x, float y,
//        float originX, float originY,
//        float width, float height,
//        float scaleX, float scaleY,
//        float rotation);


        float width = normal.getRegionWidth() * TokyoRushGame.scale;
        float height = normal.getRegionHeight() * TokyoRushGame.scale;

        float originX = width * 0.5f;
        float originY = height * 0.5f;

        final float rotation = 0f;

        float x = pos.x - width * 0.5f;
        float y = pos.y - height * 0.5f;


        batch.draw(shadow, x + shadowDistance, y - shadowDistance, originX, originY, width, height, scaleShadow, scaleShadow, rotation);
        batch.draw(normal, x, y, originX, originY, width, height, scale, scale, rotation);
    }
}

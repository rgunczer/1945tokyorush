package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    Sprite hit;
    Sprite normal;
    Sprite shadow;

    float takeOffSpeed = 400f;

    Animation propellerAnim;

    Vector2 posTakeOff = new Vector2();

    Vector2 pos = new Vector2();
    Vector2 shadowOffset = new Vector2();

    PlayerStateEnum state;

    public void create() {
        state = PlayerStateEnum.IDLE_ON_AIRFIELD;
        final String textureName = "player-and-propeller.png";

        texture = new Texture(textureName);
        hit = new Sprite(texture, 0, 0, 168, 168);
        normal = new Sprite(texture, 168, 0, 168, 168);
        shadow = new Sprite(texture, 336, 0, 168, 168);

        TextureRegion prop0 = new TextureRegion(texture, 0f, 168f, 168f, 84f);
        TextureRegion prop1 = new TextureRegion(texture, 168f, 168f, 168f, 84f);
        TextureRegion prop2 = new TextureRegion(texture, 336f, 168f, 168f, 84f);

        propellerAnim = new Animation(1f, prop0, prop1, prop2);
        propellerAnim.setPlayMode(Animation.PlayMode.LOOP);

        OrthographicCamera camera = TokyoRushGame.camera;

        float w = normal.getWidth();
        float h = normal.getHeight();
        float x = (camera.viewportWidth / 2f) - w * .5f;
        float y = 0f;

        pos.x = x;
        pos.y = y;

        shadowOffset.x =  10f * TokyoRushGame.scale;
        shadowOffset.y = -10f * TokyoRushGame.scale;

        normal.setBounds(pos.x, pos.y, w, h);
        shadow.setBounds(pos.x + shadowOffset.x, pos.y + shadowOffset.y, w, h);

        posTakeOff.x = pos.x;
        posTakeOff.y = pos.y;
    }

    public void setOnAirfield() {
        pos.x = posTakeOff.x;
        pos.y = posTakeOff.y;
        setPos(pos.x, pos.y);
        takeOffSpeed = 400f;
    }

    public void update(float delta) {
        switch (state) {
            case TAKEOFF:
                pos.y += takeOffSpeed * delta;
                takeOffSpeed += 10f;
                break;
        }

        setPos(pos.x, pos.y);
    }

    public void setPos(float x, float y) {
        normal.setPosition(x, y);
        shadow.setPosition(x + shadowOffset.x, y + shadowOffset.y);
    }

    public void render(SpriteBatch batch) {
        switch (state) {
            case IDLE_ON_AIRFIELD:
                shadow.draw(batch);
                normal.draw(batch);
                break;

            case TAKEOFF:
                shadow.draw(batch);
                normal.draw(batch);
                break;
        }
    }
}

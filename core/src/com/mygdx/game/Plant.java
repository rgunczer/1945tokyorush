package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Plant {

    public static int PALM0 = 0;
    public static int PALM1 = 1;
    public static int PALM2 = 2;
    public static int PALM3 = 3;

    public static int BUSH0 = 4;
    public static int BUSH1 = 5;
    public static int BUSH2 = 6;
    public static int BUSH3 = 7;

    int type;
    TextureRegion body;
    TextureRegion shadow;

    public Vector2 pos;

    public float shadowDistance = 20f;

    public float rot = 0f;
    public float scale = 1f;

    Plant(int type, TextureRegion body, TextureRegion shadow) {
        this.type = type;
        this.body = body;
        this.shadow = shadow;
        pos = new Vector2();
    }

    Plant(Plant other) {
        this.type = other.type;
        this.body = other.body;
        this.shadow = other.shadow;
        pos = new Vector2();
    }

    public void update(float scrollY) {
        pos.y += scrollY;
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        //rot += 0.1f;
        //batch.draw(shadow, x + 10f, y - 10f, shadow.getRegionWidth(), shadow.getRegionHeight());
        //batch.draw(body, x, y, body.getRegionWidth(), body.getRegionHeight());

//        public void draw (TextureRegion region,
//        float x, float y,
//        float originX, float originY,
//        float width, float height,
//        float scaleX, float scaleY,
//        float rotation);

        float width = body.getRegionWidth() * TokyoRushGame.scale;
        float height = body.getRegionHeight() * TokyoRushGame.scale;

        float originX = width * 0.5f;
        float originY = height * 0.5f;

        float scaleX = scale;
        float scaleY = scale;

        float rotation = rot;

        batch.draw(shadow, pos.x + offset.x + shadowDistance, pos.y + offset.y - shadowDistance, originX, originY, width, height, scaleX, scaleY, rotation);
        batch.draw(body, pos.x + offset.x, pos.y + offset.y, originX, originY, width, height, scaleX, scaleY, rotation);

    }

}

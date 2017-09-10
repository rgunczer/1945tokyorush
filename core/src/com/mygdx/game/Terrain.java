package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Terrain {

    Texture texture;
    TextureRegion region;

    int rowCount = 6;
    float w;
    float h;

    Array<Vector2> positions;

    public void init() {
        float scale = TokyoRushGame.scale;
        positions = new Array<Vector2>(rowCount);
        texture = new Texture("terrain_jungle.png");
        region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        w = region.getRegionWidth() * scale;
        h = region.getRegionHeight() * scale;

        for(int i = 0; i < rowCount; ++i) {
            positions.add(new Vector2(0f, i * h));
        }
    }

    public void update(float scrollSpeedY) {
        Vector2 pos;
        for(int i = 0; i < rowCount; ++i) {
            pos = positions.get(i);
            pos.y +=  scrollSpeedY;

            if (pos.y < -h) {
                pos.y = (rowCount - 1) * h;
            }
        }
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        Vector2 pos;
        for(int i = 0; i < rowCount; ++i) {
            pos = positions.get(i);
            batch.draw(region, pos.x + offset.x, pos.y + offset.y, w, h);
        }
    }

}

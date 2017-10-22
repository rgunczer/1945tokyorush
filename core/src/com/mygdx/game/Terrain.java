package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Terrain {

    String[] keys;
    Texture[] textures;
    TextureRegion region;

    int rowCount = 6;
    float w;
    float h;

    Array<Vector2> positions;

    public void create() {
        final int size = 4;

        textures = new Texture[size];
        textures[0] = new Texture("terrain_jungle.png");
        textures[1] = new Texture("terrain_jungle_dark.png");
        textures[2] = new Texture("terrain_water.png");
        textures[3] = new Texture("terrain_water_green.png");

        keys = new String[size];
        keys[0] = "TerrainJungle";
        keys[1] = "TerrainJungleDark";
        keys[2] = "TerrainWater";
        keys[3] = "TerrainWaterGreen";
    }

    private Texture getTextureFromTerrainTypeName(String typeName) {
        for(int i = 0; i < keys.length; ++i) {
            if (keys[i].equals(typeName)) {
                return textures[i];
            }
        }
        return null;
    }

    public void init(String terrainTypeName) {
        float scale = TokyoRushGame.scale;
        positions = new Array<Vector2>(rowCount);

        Texture texture = getTextureFromTerrainTypeName(terrainTypeName);
        if (texture == null) {
            System.out.println("Terrain Texture NOT found for: [" + terrainTypeName + "]");
        }

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

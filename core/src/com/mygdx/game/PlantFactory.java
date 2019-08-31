package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class PlantFactory {

    Texture texture;
    Array<Plant> plants;

    public void create() {
        plants = new Array<Plant>(12);
        texture = TextureFactory.create("plants.png");

        TextureRegion body;
        TextureRegion shadow;


        body = new TextureRegion(texture, 0, 0, 128, 128);
        shadow = new TextureRegion(texture, 0, 128, 128, 128);
        plants.add(new Plant(Plant.PALM0, body, shadow));

        body = new TextureRegion(texture, 128, 0, 128, 128);
        shadow = new TextureRegion(texture, 128, 128, 128, 128);
        plants.add(new Plant(Plant.PALM1, body, shadow));

        body = new TextureRegion(texture, 256, 0, 128, 128);
        shadow = new TextureRegion(texture, 256, 128, 128, 128);
        plants.add(new Plant(Plant.PALM2, body, shadow));

        body = new TextureRegion(texture, 384, 0, 128, 128);
        shadow = new TextureRegion(texture, 384, 128, 128, 128);
        plants.add(new Plant(Plant.PALM3, body, shadow));

        body = new TextureRegion(texture, 512, 0, 128, 128);
        shadow = new TextureRegion(texture, 512, 128, 128, 128);
        plants.add(new Plant(Plant.BUSH0, body, shadow));

        body = new TextureRegion(texture, 640, 0, 128, 128);
        shadow = new TextureRegion(texture, 640, 128, 128, 128);
        plants.add(new Plant(Plant.BUSH1, body, shadow));

        body = new TextureRegion(texture, 768, 0, 128, 128);
        shadow = new TextureRegion(texture, 768, 128, 128, 128);
        plants.add(new Plant(Plant.BUSH2, body, shadow));

        body = new TextureRegion(texture, 896, 0, 128, 128);
        shadow = new TextureRegion(texture, 896, 128, 128, 128);
        plants.add(new Plant(Plant.BUSH3, body, shadow));
    }

    public Plant get(int type) {
        return plants.get(type);
    }

    public Plant get(String type) {
        if (type.equals("palm0")) {
            return get(0);
        }

        if (type.equals("palm1")) {
            return get(1);
        }

        if (type.equals("palm2")) {
            return get(2);
        }

        if (type.equals("palm3")) {
            return get(3);
        }

        if (type.equals("bush0")) {
            return get(4);
        }

        if (type.equals("bush1")) {
            return get(5);
        }

        if (type.equals("bush2")) {
            return get(6);
        }

        if (type.equals("bush3")) {
            return get(7);
        }

        return null;
    }

}

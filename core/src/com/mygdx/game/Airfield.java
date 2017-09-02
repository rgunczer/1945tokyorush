package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Airfield {

    boolean showArmors = false;
    boolean showGuns = false;

    Texture airfieldTexture;
    Sprite airfield;
    Sprite gun1;
    Sprite gun2;
    Sprite gun3;
    Sprite bomb2;
    Sprite bomb1;
    Sprite bomb3;
    Sprite armour1;
    Sprite armour2;
    Sprite armour3;
    Sprite engine3;
    Sprite engine2;
    Sprite engine1;

    OrthographicCamera camera;

    public void create(OrthographicCamera camera) {

        this.camera = camera;
        airfieldTexture = new Texture("airfield.png");

        airfield = new Sprite(airfieldTexture, 250, 0, 320, 568);

        gun1 = new Sprite(airfieldTexture,  0, 0, 70, 90);
        gun2 = new Sprite(airfieldTexture, 70, 0, 70, 90);
        gun3 = new Sprite(airfieldTexture, 140,0, 70, 90);

        bomb2 = new Sprite(airfieldTexture, 0,90,70,90);
        bomb1 = new Sprite(airfieldTexture, 70,90,70,90);
        bomb3 = new Sprite(airfieldTexture, 70,360,70,90);

        armour1 = new Sprite(airfieldTexture, 140,90,70,90);
        armour2 = new Sprite(airfieldTexture, 0,180,70,90);
        armour3 = new Sprite(airfieldTexture, 70,180,70,90);

        engine3 = new Sprite(airfieldTexture, 140,180,70,90);
        engine2 = new Sprite(airfieldTexture, 0,270,70,90);
        engine1 = new Sprite(airfieldTexture, 70,270,70,90);


        float scale = camera.viewportWidth / airfield.getWidth();
        float h = airfield.getHeight() * scale;
        float y = camera.viewportHeight - h;

        airfield.setBounds(0, y, camera.viewportWidth, h);

        y = 95f;
        gun1.setBounds(112f * scale, y * scale, gun1.getWidth() * scale, gun1.getHeight() * scale);
        gun2.setBounds(181f * scale, y * scale, gun2.getWidth() * scale, gun2.getHeight() * scale);
        gun3.setBounds(250f * scale, y * scale, gun3.getWidth() * scale, gun3.getHeight() * scale);

        y = 190f;
        armour1.setBounds(137f * scale, y * scale, armour1.getWidth() * scale, armour1.getHeight() * scale);
        armour2.setBounds(70f * scale, y * scale, armour2.getWidth() * scale, armour2.getHeight() * scale);
        armour3.setBounds(2f * scale, y * scale, armour3.getWidth() * scale, armour3.getHeight() * scale);
    }

    public void render(SpriteBatch batch) {

        airfield.draw(batch);

        if (showGuns) {
            gun1.draw(batch);
            gun2.draw(batch);
            gun3.draw(batch);
        }

        if (showArmors) {
            armour1.draw(batch);
            armour2.draw(batch);
            armour3.draw(batch);
        }


    }

    public void touchDown(Vector3 position) {
        if (position.x > 600f && position.x < camera.viewportWidth) {
            showArmors = !showArmors;
        }

        if (position.x > 0 && position.x < 260f) {
            showGuns = !showGuns;
        }
    }
}

package com.mygdx.game;


import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Airfield extends Screen {

    boolean showArmors = false;
    boolean showGuns = false;
    boolean showEngines = false;
    boolean showBombs = false;

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

    float gunsY;
    float armorsY;
    float enginesY;
    float bombsY;


    public void create(OrthographicCamera camera) {
        int sc = 2;
        this.camera = camera;
        airfieldTexture = new Texture("airfield2.png");

        airfield = new Sprite(airfieldTexture, 250*sc, 0, 640, 1440);

        gun1 = new Sprite(airfieldTexture,  0, 0, 70*sc, 90*sc);
        gun2 = new Sprite(airfieldTexture, 70*sc, 0, 70*sc, 90*sc);
        gun3 = new Sprite(airfieldTexture, 140*sc,0, 70*sc, 90*sc);

        bomb2 = new Sprite(airfieldTexture, 0*sc,90*sc,70*sc,90*sc);
        bomb1 = new Sprite(airfieldTexture, 70*sc,90*sc,70*sc,90*sc);
        bomb3 = new Sprite(airfieldTexture, 70*sc,360*sc,70*sc,90*sc);

        armour1 = new Sprite(airfieldTexture, 140*sc,90*sc,70*sc,90*sc);
        armour2 = new Sprite(airfieldTexture, 0*sc,180*sc,70*sc,90*sc);
        armour3 = new Sprite(airfieldTexture, 70*sc,180*sc,70*sc,90*sc);

        engine3 = new Sprite(airfieldTexture, 140*sc,180*sc,70*sc,90*sc);
        engine2 = new Sprite(airfieldTexture, 0*sc,270*sc,70*sc,90*sc);
        engine1 = new Sprite(airfieldTexture, 70*sc,270*sc,70*sc,90*sc);


        float scale = camera.viewportWidth / airfield.getWidth();
        float w = airfield.getWidth() * scale;
        float h = airfield.getHeight() * scale;
        float y = 0f;
        float x = 0f;

        airfield.setBounds(x, y, w, h);

        float gap = 3f * scale;

        w = gun1.getWidth() * scale;
        x = (camera.viewportWidth / 3f);
        gunsY = y = 390f;
        gun1.setBounds(x,                 y * scale, gun1.getWidth() * scale, gun1.getHeight() * scale);
        gun2.setBounds(x + ((w + gap) * 1) , y * scale, gun2.getWidth() * scale, gun2.getHeight() * scale);
        gun3.setBounds(x + ((w + gap) * 2), y * scale, gun3.getWidth() * scale, gun3.getHeight() * scale);

        x = ((camera.viewportWidth / 3f) * 2f) - w;
        armorsY = y = 190f;
        armour1.setBounds(x,                   y * scale, armour1.getWidth() * scale, armour1.getHeight() * scale);
        armour2.setBounds(x - ((w + gap) * 1), y * scale, armour2.getWidth() * scale, armour2.getHeight() * scale);
        armour3.setBounds(x - ((w + gap) * 2), y * scale, armour3.getWidth() * scale, armour3.getHeight() * scale);


        x = (camera.viewportWidth / 3f);
        bombsY = y = 777f;
        bomb1.setBounds(x,                 y * scale, bomb1.getWidth() * scale, bomb1.getHeight() * scale);
        bomb2.setBounds(x + ((w + gap) * 1) , y * scale, bomb2.getWidth() * scale, bomb2.getHeight() * scale);
        bomb3.setBounds(x + ((w + gap) * 2), y * scale, bomb3.getWidth() * scale, bomb3.getHeight() * scale);


        x = ((camera.viewportWidth / 3f) * 2f) - w;
        enginesY = y = 585f;
        engine1.setBounds(x,                   y * scale, engine1.getWidth() * scale, engine1.getHeight() * scale);
        engine2.setBounds(x - ((w + gap) * 1), y * scale, engine2.getWidth() * scale, engine2.getHeight() * scale);
        engine3.setBounds(x - ((w + gap) * 2), y * scale, engine3.getWidth() * scale, engine3.getHeight() * scale);


        armorsY *= scale;
        gunsY *= scale;
        bombsY *= scale;
        enginesY *= scale;
    }

    @Override
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

        if (showBombs) {
            bomb1.draw(batch);
            bomb2.draw(batch);
            bomb3.draw(batch);
        }

        if (showEngines) {
            engine1.draw(batch);
            engine2.draw(batch);
            engine3.draw(batch);
        }
    }

    private boolean isInsideY(Vector3 position, float y) {
        float oneThird = camera.viewportWidth / 3f;
        float upperY = y + oneThird;
        float lowerY = y;
        if ((position.y > lowerY) && (position.y < upperY)) {
            return true;
        }
        return false;
    }

    public void touchDown(Vector3 position) {
        System.out.println("touchdown vec3 position x: " + position.x + ", y: " + position.y);
        float oneThird = camera.viewportWidth / 3f;
        if (position.x > (oneThird * 2f) && position.x < camera.viewportWidth) {
            if (isInsideY(position, armorsY)) {
                showArmors = !showArmors;
            }

            if (isInsideY(position, enginesY)) {
                showEngines = !showEngines;
            }
        }

        if (position.x > 0 && position.x < oneThird) {
            if (isInsideY(position, gunsY)) {
                showGuns = !showGuns;
            }

            if (isInsideY(position, bombsY)) {
                showBombs = !showBombs;
            }
        }
    }
}

package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu extends Screen {

    Texture mainMenuTexture;
    Sprite bg;

    OrthographicCamera camera;
    float alpha = 0f;
    float direction = 1f;


    public void create(OrthographicCamera camera) {
        this.camera = camera;

        mainMenuTexture = new Texture("main_menu.png");



        bg = new Sprite(mainMenuTexture, 660, 0, 640, 1440);


        float scale = camera.viewportWidth / bg.getWidth();
        float w = bg.getWidth() * scale;
        float h = bg.getHeight() * scale;
        float y = 0f;
        float x = 0f;

        bg.setBounds(x, y, w, h);

    }

    @Override
    public void render(SpriteBatch batch) {
        alpha += 0.01f * direction;
        if (alpha > 1f) {
            alpha = 1f;
            direction *= -1f;
        } else if (alpha < 0) {
            alpha = 0f;
            direction *= -1f;
        }

        System.out.println("alpha: " + alpha);


        float w = camera.viewportWidth / 2f;
        float h = camera.viewportWidth / 3f;

        //shapeRenderer.rect( (camera.viewportWidth / 2f) - (w / 2f), camera.viewportHeight / 2f, w, h);

        bg.draw(batch);

    }
}

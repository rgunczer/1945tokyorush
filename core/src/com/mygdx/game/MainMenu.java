package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MainMenu extends Screen {

    Texture mainMenuTexture;
    Sprite background;

    OrthographicCamera camera;
    float alpha = 0f;
    float direction = 1f;


    public void create(OrthographicCamera camera) {
        this.camera = camera;

        mainMenuTexture = new Texture("main_menu.png");



        background = new Sprite(mainMenuTexture, 660, 0, 640, 1440);


        float scale = camera.viewportWidth / background.getWidth();
        float w = background.getWidth() * scale;
        float h = background.getHeight() * scale;
        float y = 0f;
        float x = 0f;

        background.setBounds(x, y, w, h);
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

        //System.out.println("alpha: " + alpha);

        float w = camera.viewportWidth / 2f;
        float h = camera.viewportWidth / 3f;

        //shapeRenderer.rect( (camera.viewportWidth / 2f) - (w / 2f), camera.viewportHeight / 2f, w, h);

        background.draw(batch);
    }

    @Override
    public void touchDown(Vector3 position) {
        System.out.println("touchdown vec3 position x: " + position.x + ", y: " + position.y);
        float oneThird = camera.viewportWidth / 3f;

        if (position.x < oneThird && position.y < oneThird) {
            game.showScreen(TokyoRushGame.ScreenEnum.AIRFIELD);
        }
    }
}

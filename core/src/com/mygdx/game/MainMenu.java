package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainMenu {

    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    float alpha = 0f;
    float direction = 1f;


    public void create(OrthographicCamera camera) {
        this.camera = camera;
        shapeRenderer = new ShapeRenderer();

    }

    public void render() {
        alpha += 0.01f * direction;
        if (alpha > 1f) {
            alpha = 1f;
            direction *= -1f;
        } else if (alpha < 0) {
            alpha = 0f;
            direction *= -1f;
        }

        System.out.println("alpha: " + alpha);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 1f, 0f, alpha);

        float w = camera.viewportWidth / 2f;
        float h = camera.viewportWidth / 3f;

        //shapeRenderer.rect( (camera.viewportWidth / 2f) - (w / 2f), camera.viewportHeight / 2f, w, h);


        shapeRenderer.rect(0f, 0f, camera.viewportWidth, camera.viewportHeight);

        shapeRenderer.end();
    }
}

package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuTitle {

    Texture texture;
    Sprite sprite;

    public MenuTitle(String textureName) {
        texture = new Texture(textureName);
        sprite = new Sprite(texture);
    }

    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}

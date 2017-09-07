package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuItem {

    Texture texture;
    public Sprite sprite;

    public MenuItem(String textureName) {
        texture = new Texture(textureName);
        sprite = new Sprite(texture);
    }

    public void setBounds(float x, float y, float w, float h) {
        sprite.setBounds(x, y, w, h);
    }

    public void setPos(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}

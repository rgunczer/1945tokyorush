package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuItem {

    private Texture texture;
    public Sprite sprite;
    MainMenuScreen.MenuAction action;

    public MenuItem(String textureName, MainMenuScreen.MenuAction action) {
        texture = new Texture(textureName);
        sprite = new Sprite(texture);
        this.action = action;
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

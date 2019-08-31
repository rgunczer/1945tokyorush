package com.mygdx.game.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TextureFactory;
import com.mygdx.game.screens.MainMenuScreen;

public class MenuItem {

    private Texture texture;
    public Sprite sprite;
    public MainMenuScreen.MenuAction action;

    public MenuItem(String textureName, MainMenuScreen.MenuAction action) {
        texture = TextureFactory.create(textureName);
        sprite = new Sprite(texture);
        this.action = action;
    }

    public void setBounds(float x, float y, float w, float h) {
        sprite.setBounds(x, y, w, h);
    }

    public void setPos(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}

package com.mygdx.game.menu;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TextureFactory;

public class MenuTitle {

    Texture texture;
    Sprite sprite;

    public MenuTitle(String textureName) {
        texture = TextureFactory.create(textureName);
        sprite = new Sprite(texture);
    }

    public void update(float delta) {

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}

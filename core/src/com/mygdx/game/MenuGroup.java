package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;


public class MenuGroup {

    MenuTitle menuTitle;
    Array<MenuItem> menuItems;

    public MenuGroup() {
        menuItems = new Array<MenuItem>(12);
    }

    public void add(MenuItem menuItem) {
        menuItems.add(menuItem);
        calcMenuItemPositions();
    }

    public void add(MenuTitle menuTitle) {
        this.menuTitle = menuTitle;

        OrthographicCamera camera = TokyoRushGame.camera;

        float scale = TokyoRushGame.scale;
        float w = TokyoRushGame.referenceWidth * scale;
        float h = TokyoRushGame.referenceHeight * scale;
        float y = 0f;
        float x = 0f;

        y = camera.viewportHeight - menuTitle.sprite.getHeight() * scale;
        menuTitle.sprite.setBounds(x, y, w, menuTitle.sprite.getHeight() * scale);
    }

    private void calcMenuItemPositions() {
        float x;
        float y = 0.5f;
        OrthographicCamera camera = TokyoRushGame.camera;
        float scale = TokyoRushGame.scale;
        float w = TokyoRushGame.referenceWidth * scale;
        float h = TokyoRushGame.referenceHeight * scale;

        for(MenuItem menuItem: menuItems) {
            menuItem.sprite.setBounds(0f, 0f, w, menuItem.sprite.getRegionHeight() * scale);
            menuItem.sprite.setPosition(0f, camera.viewportHeight * y);
            y -= 0.12f;
        }

    }

    public void update(float delta) {

    }

    public void render(SpriteBatch batch) {
        menuTitle.draw(batch);

        for(MenuItem menuItem : menuItems) {
            menuItem.draw(batch);
        }
    }
}

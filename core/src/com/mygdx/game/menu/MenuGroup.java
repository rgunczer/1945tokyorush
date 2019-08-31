package com.mygdx.game.menu;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.TokyoRushGame;


public class MenuGroup {

    MenuTitle menuTitle;
    public Array<MenuItem> menuItems;
    float gapBetweenMenuItemsY = 0.12f;

    public MenuGroup() {
        menuItems = new Array<MenuItem>(12);
    }

    public void add(MenuItem menuItem) {
        menuItems.insert(0, menuItem);
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
        OrthographicCamera camera = TokyoRushGame.camera;
        float x = 0f;
        float y = gapBetweenMenuItemsY * 0.5f;
        float scale = TokyoRushGame.scale;
        float w = TokyoRushGame.referenceWidth * scale;
        float h = TokyoRushGame.referenceHeight * scale;

        for(MenuItem menuItem: menuItems) {
            menuItem.sprite.setBounds(x, 0f, w, menuItem.sprite.getRegionHeight() * scale);
            menuItem.sprite.setPosition(x, camera.viewportHeight * y);
            y += gapBetweenMenuItemsY;
        }

    }

    public void update(float delta) {
        for(MenuItem menuItem : menuItems) {
            menuItem.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        menuTitle.draw(batch);

        for(MenuItem menuItem : menuItems) {
            menuItem.draw(batch);
        }
    }
}

package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MainMenu extends Screen {

    Texture mainMenuTexture;
    Sprite background;

    Texture menuTitleTexture;
    Sprite menuTitle;

    Array<MenuItem> menuItems;

    MenuItem menuItemPlay;
    MenuItem menuItemHiScores;
    MenuItem menuItemSettings;
    MenuItem menuItemAbout;

    OrthographicCamera camera;
    float alpha = 0f;
    float direction = 1f;


    public void create(OrthographicCamera camera) {
        this.camera = camera;

        menuItems = new Array<MenuItem>(12);

        mainMenuTexture = new Texture("main_menu.png");
        menuTitleTexture = new Texture("menu-title.png");

        menuTitle = new Sprite(menuTitleTexture);

        background = new Sprite(mainMenuTexture, 660, 0, 640, 1440);

        menuItemPlay = new MenuItem("menu-item-play.png");
        menuItemHiScores = new MenuItem("menu-item-hiscores.png");
        menuItemSettings = new MenuItem("menu-item-settings.png");
        menuItemAbout = new MenuItem("menu-item-about.png");

        float scale = camera.viewportWidth / background.getWidth();
        float w = background.getWidth() * scale;
        float h = background.getHeight() * scale;
        float y = 0f;
        float x = 0f;

        background.setBounds(x, y, w, h);

        y = camera.viewportHeight - menuTitle.getHeight() * scale;
        menuTitle.setBounds(x, y, w, menuTitle.getHeight() * scale);


        menuItemPlay.setBounds(0f, 0f, w, menuItemPlay.sprite.getHeight() * scale);
        menuItemPlay.setPos(0f, camera.viewportHeight * 0.5f);


        menuItemHiScores.setBounds(0f, 0f, w, menuItemSettings.sprite.getHeight() * scale);
        menuItemHiScores.setPos(0f, camera.viewportHeight * 0.4f);

        menuItemSettings.setBounds(0f, 0f, w, menuItemSettings.sprite.getHeight() * scale);
        menuItemSettings.setPos(0f, camera.viewportHeight * 0.3f);

        menuItemAbout.setBounds(0f, 0f, w, menuItemAbout.sprite.getHeight() * scale);
        menuItemAbout.setPos(0f, camera.viewportHeight * 0.2f);

        menuItems.add(menuItemPlay);
        menuItems.add(menuItemHiScores);
        menuItems.add(menuItemSettings);
        menuItems.add(menuItemAbout);
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
        menuTitle.draw(batch);

        for(MenuItem menuItem : menuItems) {
            menuItem.draw(batch);
        }

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

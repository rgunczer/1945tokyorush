package com.mygdx.game.screens;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.menu.MenuGroup;
import com.mygdx.game.menu.MenuItem;
import com.mygdx.game.menu.MenuState;
import com.mygdx.game.menu.MenuTitle;
import com.mygdx.game.TextureFactory;
import com.mygdx.game.TokyoRushGame;

public class MainMenuScreen extends BaseScreen {

    public enum MenuAction {
        PLAY,
        HISCORES,
        SETTINGS,
        ABOUT,
        EASY,
        NORMAL,
        HARD,
        BACK
    }

    Texture backgroundTexture;
    Sprite background;

    MenuGroup mainMenu;
    MenuGroup difficultyMenu;
    MenuGroup hiScoresMenu;
    MenuGroup settingsMenu;
    MenuGroup aboutMenu;

    MenuGroup currentMenu;


    @Override
    public void create() {
        backgroundTexture = TextureFactory.create("main_menu.png");
        background = new Sprite(backgroundTexture, 660, 0, 640, 1440);

        mainMenu = new MenuGroup();
        mainMenu.add(new MenuItem("menu-item-play.png", MenuAction.PLAY));
        mainMenu.add(new MenuItem("menu-item-hiscores.png", MenuAction.HISCORES));
        mainMenu.add(new MenuItem("menu-item-settings.png", MenuAction.SETTINGS));
        mainMenu.add(new MenuItem("menu-item-about.png", MenuAction.ABOUT));
        mainMenu.add(new MenuTitle("menu-title.png"));

        difficultyMenu = new MenuGroup();
        difficultyMenu.add(new MenuItem("menu-item-easy.png", MenuAction.EASY));
        difficultyMenu.add(new MenuItem("menu-item-normal.png", MenuAction.NORMAL));
        difficultyMenu.add(new MenuItem("menu-item-hard.png", MenuAction.HARD));
        difficultyMenu.add(new MenuItem("menu-item-back.png", MenuAction.BACK));
        difficultyMenu.add(new MenuTitle("menu-title-difficulty.png"));

        hiScoresMenu = new MenuGroup();
        hiScoresMenu.add(new MenuItem("menu-item-back.png", MenuAction.BACK));
        hiScoresMenu.add(new MenuTitle("menu-title-hiscores.png"));

        settingsMenu = new MenuGroup();
        settingsMenu.add(new MenuItem("menu-item-back.png", MenuAction.BACK));
        settingsMenu.add(new MenuTitle("menu-title-settings.png"));

        aboutMenu = new MenuGroup();
        aboutMenu.add(new MenuItem("menu-item-back.png", MenuAction.BACK));
        aboutMenu.add(new MenuTitle("menu-title-about.png"));

        float scale = TokyoRushGame.scale;
        float w = background.getWidth() * scale;
        float h = background.getHeight() * scale;
        float y = 0f;
        float x = 0f;

        background.setBounds(x, y, w, h);

        currentMenu = mainMenu;
    }

    @Override
    public void init() {
        currentMenu = mainMenu;
    }

    @Override
    public void update(float delta) {
        currentMenu.update(delta);
    }

    @Override
    public void render(float dt) {
        beginRender();
        background.draw(batch);
        currentMenu.render(batch);
        endRender();
    }

    @Override
    public void touchDown(Vector3 position) {
        System.out.println("touchdown vec3 position x: " + position.x + ", y: " + position.y);

        for(MenuItem menuItem: currentMenu.menuItems) {
            boolean hit = menuItem.sprite.getBoundingRectangle().contains(position.x, position.y);
            if (hit) {
               switch (menuItem.action) {
                   case PLAY:
                       currentMenu = difficultyMenu;
                       break;

                   case HISCORES:
                       currentMenu = hiScoresMenu;
                       break;

                   case SETTINGS:
                       currentMenu = settingsMenu;
                       break;

                   case ABOUT:
                       currentMenu = aboutMenu;
                       break;

                   case BACK:
                       mainMenu.setState(MenuState.ANIM_APPEAR);
                       currentMenu = mainMenu;
                       break;

                   case EASY:
                       TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.AIRFIELD);
                       break;

                   case NORMAL:
                       TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.AIRFIELD);
                       break;

                   case HARD:
                       TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.AIRFIELD);
                       break;
               }
            }
        }
    }

    @Override
    public void touchMove(Vector3 position) {}

}

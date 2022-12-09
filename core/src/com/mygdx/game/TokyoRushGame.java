package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.screens.AirfieldScreen;
import com.mygdx.game.screens.BaseScreen;
import com.mygdx.game.screens.LevelScreen;
import com.mygdx.game.screens.MainMenuScreen;


public class TokyoRushGame extends ApplicationAdapter implements InputProcessor {

    public enum ScreenEnum {
        MAIN_MENU,
        AIRFIELD,
        LEVEL
    }

    public static float referenceWidth = 640f;
    public static float referenceHeight = 1440f;

    public static LevelScreen levelScreen;
    public static AirfieldScreen airfieldScreen;
    public static MainMenuScreen mainMenuScreen;
    public static BaseScreen currentScreen;

    public static Player player;
    public static OrthographicCamera camera;
    public static float scale;
    public static TokyoRushGame instance;

    public static void showScreen(ScreenEnum screen) {
        switch (screen) {
            case AIRFIELD:
                currentScreen = airfieldScreen;
                break;

            case MAIN_MENU:
                currentScreen = mainMenuScreen;
                break;

            case LEVEL:
                currentScreen = levelScreen;
                break;
        }
        currentScreen.init();
    }

	@Override
	public void create () {
        instance = this;

        Gdx.input.setInputProcessor(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        scale = camera.viewportWidth / 640f;

        // screens
        airfieldScreen = new AirfieldScreen();
        mainMenuScreen = new MainMenuScreen();
        player = new Player();
        levelScreen = new LevelScreen();

        // create
        airfieldScreen.create();
        mainMenuScreen.create();
        player.create();
        levelScreen.create();

        // init
//        levelScreen.init();

        currentScreen = mainMenuScreen;
	}

	@Override
	public void render () {
        float dt = Gdx.graphics.getDeltaTime();
        currentScreen.update(dt);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentScreen.render(dt);
	}
	
	@Override
	public void dispose () {
        //batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0)); // TODO: fix GC
        currentScreen.touchDown(position);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0)); // TODO: fix GC
        currentScreen.touchMove(position);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}

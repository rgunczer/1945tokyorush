package com.almagems.tokyorush;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

import com.almagems.tokyorush.screens.AirfieldScreen;
import com.almagems.tokyorush.screens.BaseScreen;
import com.almagems.tokyorush.screens.LevelScreen;
import com.almagems.tokyorush.screens.MainMenuScreen;


public class TokyoRushGame extends ApplicationAdapter implements InputProcessor {
//public class TokyoRushGame extends ApplicationAdapter {

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

    private Vector3 touchPosition = new Vector3();

    public record Bullet(int x, int y) {}


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

        final var bullet = new Bullet(10,12);
        System.out.println(bullet);

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
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
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
        camera.unproject(touchPosition.set(screenX, screenY, 0));
        currentScreen.touchDown(touchPosition);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.unproject(touchPosition.set(screenX, screenY, 0));
        currentScreen.touchMove(touchPosition);
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

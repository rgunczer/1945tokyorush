package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;


public class TokyoRushGame extends ApplicationAdapter implements InputProcessor {

    public enum ScreenEnum {
        MAIN_MENU,
        AIRFIELD,
        LEVEL
    }

    private long lastTime;


    public static float referenceWidth = 640f;
    public static float referenceHeight = 1440f;

    public static AirfieldScreen airfieldScreen;
    public static MainMenuScreen mainMenuScreen;
    public static Screen currentScreen;

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

                break;
        }
    }

	@Override
	public void create () {
        Gdx.input.setInputProcessor(this);

        instance = this;

        airfieldScreen = new AirfieldScreen();
        mainMenuScreen = new MainMenuScreen();
        player = new Player();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        scale = camera.viewportWidth / 640f;

        airfieldScreen.create();
        mainMenuScreen.create();
        player.create();

        lastTime = TimeUtils.millis();

        currentScreen = mainMenuScreen;
	}

	@Override
	public void render () {
        update();

		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentScreen.render();
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
	}

	private void update() {
        //long elapsedTime = TimeUtils.timeSinceMillis(lastTime);

        //System.out.println("elapsed time: " + elapsedTime);
        float elapsedTime = 1f / 60f;
        currentScreen.update(elapsedTime);
        //lastTime = TimeUtils.millis();

/*
        if (now - lastSpawn > SPAWN_DELTA) {
            lastSpawn = now;
            int column = MathUtils.random(0, COLUMNS - 1);

            Sprite block = new Sprite(img);
            block.setBounds(column * blockSize, -blockSize, blockSize, blockSize);
            block.setOriginCenter();

            blocks[column].add(block);
        }

        // loop through all Sprites
        for(int column = 0; column < blocks.length; ++column) {
            for(int i = 0; i < blocks[column].size(); ++i) {
                Sprite block = blocks[column].get(i);
                block.translateY(velocity);

                float maxY;
                if (i > 0) {
                    Sprite previous = blocks[column].get(i - 1);
                    maxY = previous.getY() - block.getHeight();
                } else {
                    maxY = camera.viewportHeight - block.getHeight();
                }

                if (block.getY() > maxY) {
                    block.setY(maxY);
                }
            }
        }
*/
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
        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
        currentScreen.touchDown(position);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

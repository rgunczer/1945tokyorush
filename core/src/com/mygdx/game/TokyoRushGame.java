package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;


public class TokyoRushGame extends ApplicationAdapter implements InputProcessor {

    public enum ScreenEnum {
        MAIN_MENU,
        AIRFIELD,
        LEVEL
    }

    SpriteBatch batch;
	Texture img;

    Texture playerHudFonts;
    Sprite player;

    Airfield airfieldScreen;
    MainMenu mainMenuScreen;
    Screen currentScreen;



    final int SPAWN_DELTA = 1000;
    final int COLUMNS = 6;

    ArrayList<Sprite>[] blocks = new ArrayList[COLUMNS];

    long lastSpawn = 0;

    OrthographicCamera camera;

    float blockSize;
    float velocity;

    public void showScreen(ScreenEnum screen) {
        switch (screen) {
            case AIRFIELD:
                currentScreen = airfieldScreen;
                break;

            case MAIN_MENU:
                currentScreen = mainMenuScreen;
                break;
        }
    }

	@Override
	public void create () {

        Screen.game = this;

        airfieldScreen = new Airfield();

        mainMenuScreen = new MainMenu();

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        playerHudFonts = new Texture("player_hud_fonts_clouds.png");

        player = new Sprite(playerHudFonts, 74, 222, 74, 54);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        for(int i = 0; i < blocks.length; ++i) {
            blocks[i] = new ArrayList<Sprite>();
        }

        blockSize = camera.viewportWidth / COLUMNS;
        velocity = camera.viewportHeight / 200;

        Gdx.input.setInputProcessor(this);

        airfieldScreen.create(camera);
        mainMenuScreen.create(camera);

        float scale = camera.viewportWidth / 640f;

        player.setOriginCenter();
        player.setScale(scale * 1.5f);
        player.setX((camera.viewportWidth / 2f) - player.getWidth() / 2f);
        player.setY(100f * scale);

        currentScreen = mainMenuScreen;
	}

	@Override
	public void render () {
        update();

		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for(int column = 0; column < blocks.length; ++column) {
            for(int i = 0; i < blocks[column].size(); ++i) {
                blocks[column].get(i).draw(batch);
            }
        }

		float x = (Gdx.graphics.getWidth() / 2f) - (img.getWidth() / 2f);
        float y = (Gdx.graphics.getHeight() / 2f) - (img.getHeight() / 2f);

		//batch.draw(img, x, y);
        currentScreen.render(batch);
        //player.draw(batch);
        //mainMenuScreen.render(batch);
        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	private void update() {
        long now = TimeUtils.millis();

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
        int y = (int)camera.viewportHeight - screenY;
        System.out.println("screenX: " + screenX + ", screenY: " + y);

        Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));

        currentScreen.touchDown(position);

//        // loop through all sprites
//        for(int column = 0; column < blocks.length; ++column) {
//            for(int i = 0; i < blocks[column].size(); ++i) {
//                Sprite block = blocks[column].get(i);
//
//                if (block.getBoundingRectangle().contains(position.x, position.y)) {
//                    block.rotate90(true);
//                    return true;
//                }
//            }
//        }

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

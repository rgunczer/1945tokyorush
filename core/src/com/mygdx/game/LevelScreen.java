package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LevelScreen extends Screen {

    final int playerBulletCount = 120;

    Terrain terrain;

    Array<Bullet> playerBullets;

    Array<Plant> plants;

    final int plantCount = 33;

    PlantFactory plantFactory;

    int fireCounter;

    Vector2 offset;
    float scrollSpeedY;

    @Override
    public void create() {
        offset = new Vector2();

        terrain = new Terrain();
        terrain.init();

        Bullet.create();

        playerBullets = new Array<Bullet>(playerBulletCount);

        for(int i = 0; i < playerBulletCount; ++i) {
            playerBullets.add(new Bullet());
        }

        plantFactory = new PlantFactory();
        plantFactory.create();

        plants = new Array<Plant>(plantCount);

        createRandomPlants();
    }

    @Override
    public void init() {
        offset.set(0f, 0f);
        scrollSpeedY = -3.5f;

        fireCounter = 0;

        TokyoRushGame.player.setOnLevel();
    }

    private void putPlantToRandomPosition(Plant plant) {
        OrthographicCamera camera = TokyoRushGame.camera;

        float extraSpace = camera.viewportWidth / 4f;

        plant.pos.x = MathUtils.random(-extraSpace, camera.viewportWidth + extraSpace);
        plant.pos.y = camera.viewportHeight + extraSpace + MathUtils.random(-extraSpace * 0.5f, extraSpace * 0.5f);

        plant.rot = MathUtils.random(0f, 360f);
        plant.scale = MathUtils.random(0.6f, 1.0f);
    }

    private void createRandomPlants() {
        OrthographicCamera camera = TokyoRushGame.camera;

        Plant template;
        Plant plant;
        for(int i = 0; i < plantCount; ++i) {
            template = plantFactory.get(MathUtils.random(0, 3));
            plant = new Plant(template);
            plant.pos.x = MathUtils.random(-camera.viewportWidth / 4f, camera.viewportWidth);
            plant.pos.y = MathUtils.random(0f, camera.viewportHeight);
            plant.rot = MathUtils.random(0f, 360f);
            plant.scale = MathUtils.random(0.4f, 1.0f);
            plants.add(plant);
        }
    }

    @Override
    public void update(float delta) {
        OrthographicCamera camera = TokyoRushGame.camera;

        terrain.update(scrollSpeedY);

        for(Plant plant: plants) {
            plant.update(scrollSpeedY);

            if (plant.pos.y < -camera.viewportWidth / 3f) {
                putPlantToRandomPosition(plant);
            }
        }

        for(Bullet bullet: playerBullets) {
            if (bullet.live) {
                bullet.update(delta);
            }
        }

        TokyoRushGame.player.update(delta);

        ++fireCounter;

        if (fireCounter >= 10) {
            playerFire();
            fireCounter = 0;
        }
    }

    private Bullet getFirstAvailableBullet() {
        Bullet bullet;
        for(int i = 0; i < playerBulletCount; ++i) {
            bullet = playerBullets.get(i);
            if (!bullet.live) {
                return bullet;
            }
        }
        return null;
    }

    private Vector2 getPlayerCenterPosition() {
        Vector2 pos = new Vector2(TokyoRushGame.player.pos);
        float width = TokyoRushGame.player.normal.getRegionWidth() * TokyoRushGame.scale;
        float height = TokyoRushGame.player.normal.getRegionHeight() * TokyoRushGame.scale;

        float originX = width * 0.5f;
        float originY = height * 0.5f;

        float x = pos.x - (width * 0.5f) + originX;
        float y = pos.y - (height * 0.5f) + originY;
        pos.set(x, y);

        return pos;
    }

    private void playerFire() {
        Vector2 playerPos = getPlayerCenterPosition();
        Bullet bullet = getFirstAvailableBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet, playerPos.x + 10f * TokyoRushGame.scale, playerPos.y, 0f, 720f * TokyoRushGame.scale, 0f);

            bullet = getFirstAvailableBullet();
            if (bullet != null) {
                bullet.set(Bullet.playerBullet, playerPos.x - 10f * TokyoRushGame.scale, playerPos.y, 0f, 720f * TokyoRushGame.scale, 0f);
            }
        }
    }

    @Override
    public void render() {
        beginRender();

        terrain.draw(batch, offset);

        for(Plant plant: plants) {
            plant.draw(batch, offset);
        }

        for(Bullet bullet: playerBullets) {
            if (bullet.live) {
                bullet.draw(batch, offset);
            }
        }


        TokyoRushGame.player.render(batch);

        endRender();
    }

    @Override
    public void touchDown(Vector3 position) {
        OrthographicCamera camera = TokyoRushGame.camera;

        TokyoRushGame.player.setTargetPosition(position.x, position.y);

        if (position.y < camera.viewportWidth / 6f) {
            TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.MAIN_MENU);
        }
    }

    @Override
    public void touchMove(Vector3 position) {
        //System.out.println("touchMove: x:" + position.x + ", y:" + position.y);
        TokyoRushGame.player.setTargetPosition(position.x, position.y);
    }
}

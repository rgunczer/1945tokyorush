package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LevelScreen extends Screen {

    Terrain terrain;

    Array<Plant> plants;

    final int plantCount = 33;

    PlantFactory plantFactory;

    Vector2 offset;
    float scrollSpeedY;

    @Override
    public void create() {
        offset = new Vector2();

        terrain = new Terrain();
        terrain.init();

        plantFactory = new PlantFactory();
        plantFactory.create();

        plants = new Array<Plant>(plantCount);

        createRandomPlants();
    }

    @Override
    public void init() {
        offset.set(0f, 0f);
        scrollSpeedY = -3.5f;

        TokyoRushGame.player.setOnLevel();
    }

    private void putPlantToRandomPosition(Plant plant) {
        OrthographicCamera camera = TokyoRushGame.camera;

        float extraSpace = camera.viewportWidth / 4f;

        plant.pos.x = MathUtils.random(-extraSpace, camera.viewportWidth + extraSpace);
        plant.pos.y += camera.viewportHeight + extraSpace;

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
        terrain.update(scrollSpeedY);

        for(Plant plant: plants) {
            plant.update(scrollSpeedY);
        }
    }

    @Override
    public void render() {
        beginRender();


        OrthographicCamera camera = TokyoRushGame.camera;

        terrain.draw(batch, offset);

        for(Plant plant: plants) {
            plant.draw(batch, offset);

            if (plant.pos.y < -camera.viewportWidth / 3f) {
                putPlantToRandomPosition(plant);
            }
        }

        TokyoRushGame.player.render(batch);

        endRender();
    }

    @Override
    public void touchDown(Vector3 position) {
        OrthographicCamera camera = TokyoRushGame.camera;
        float halfScreenX = camera.viewportWidth / 2f;
        if (position.x > halfScreenX) {
            offset.x += 10f;
        } else {
            offset.x -= 10f;
        }

        if (position.y < camera.viewportHeight / 4f) {
            TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.MAIN_MENU);
        }
    }
}

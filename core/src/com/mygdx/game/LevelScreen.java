package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LevelScreen extends Screen {

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    float playerBulletRadius;
    final int playerBulletCount = 120;

    Terrain terrain;

    Array<Bullet> playerBullets;

    Array<Plant> plants;
    Array<Tank> tanks;
    Array<Explosion> explosions;

    final int plantCount = 33;
    final int tankCount = 6;
    final int explosionCount = 33;

    PlantFactory plantFactory;
    TankFactory tankFactory;

    int fireCounter;

    Vector2 offset;
    float scrollSpeedY;

    Clouds clouds;

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

        tankFactory = new TankFactory();
        tankFactory.create();

        plants = new Array<Plant>(plantCount);
        tanks = new Array<Tank>(tankCount);
        explosions = new Array<Explosion>(explosionCount);

        Explosion.create();
        Explosion explosion;
        Vector2 pos = new Vector2();
        for(int i = 0; i < explosionCount; ++i) {
            explosion = new Explosion();
            explosion.init(pos);
        }

        Clouds.create();
        clouds = new Clouds();
    }

    @Override
    public void init() {
        playerBulletRadius = 10f * TokyoRushGame.scale;

        offset.set(0f, 0f);
        scrollSpeedY = -1.2f; //-3.5f;

        fireCounter = 0;

        TokyoRushGame.player.setOnLevel();

        for(Bullet bullet: playerBullets) {
            bullet.live = false;
        }

        for(Explosion explosion: explosions) {
            explosion.live = false;
        }

        createRandomPlants();
        createRandomTanks();
        createRandomExplosions();
        createRandomClouds();
    }

    private void createRandomClouds() {
        clouds.init();
    }

    private void createRandomExplosions() {
        Explosion explosion;
        Vector2 pos = new Vector2();
        for(int i = 0; i < explosionCount; ++i) {
            explosion = new Explosion();
            pos.x = MathUtils.random(0f, TokyoRushGame.camera.viewportWidth);
            pos.y = MathUtils.random(0f, TokyoRushGame.camera.viewportHeight);
            explosion.init(pos);
            explosion.live = false;
            explosions.add(explosion);
        }
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

    private void createRandomTanks() {
        OrthographicCamera camera = TokyoRushGame.camera;

        Tank tank;
        for (int i = 0; i < tankCount; ++i) {
            tank = tankFactory.get(Tank.TANK_TYPE_SMALL);
            tank.pos.x = MathUtils.random(-camera.viewportWidth / 4f, camera.viewportWidth);
            tank.pos.y = MathUtils.random(0f, camera.viewportHeight);
            tank.init();
            tanks.add(tank);
        }
    }

    @Override
    public void update(float delta) {
        OrthographicCamera camera = TokyoRushGame.camera;

        checkCollision();

        terrain.update(scrollSpeedY);

        for(Plant plant: plants) {
            plant.update(scrollSpeedY);

            if (plant.pos.y < -camera.viewportWidth / 3f) {
                putPlantToRandomPosition(plant);
            }
        }

        for(Tank tank: tanks) {
            tank.update(delta, scrollSpeedY);

            if (tank.pos.y < -100f || tank.pos.x < -100f * TokyoRushGame.scale || tank.pos.x > camera.viewportWidth + 100 * TokyoRushGame.scale) {
                tank.pos.x = MathUtils.random(-camera.viewportWidth / 4f, camera.viewportWidth);
                tank.pos.y = MathUtils.random(camera.viewportHeight + 200f * TokyoRushGame.scale, camera.viewportHeight + 200f * TokyoRushGame.scale);
                tank.init();
            }
        }

        for(Bullet bullet: playerBullets) {
            if (bullet.live) {
                bullet.update(delta);
            }
        }

        TokyoRushGame.player.update(delta);

        playerFire();

        Vector2 pos = new Vector2();
        for(Explosion explosion: explosions) {
            if (explosion.live) {
                explosion.update(delta, scrollSpeedY);
            }

            if (explosion.pos.y < 0f) {
                pos.x = MathUtils.random(0f, camera.viewportWidth);
                pos.y = camera.viewportHeight;
                explosion.init(pos);
            }
        }

        clouds.update(delta, scrollSpeedY*2f);
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
//        if (++fireCounter > 60) {
        if (++fireCounter > 10) {
            final float bulletSpeed = 730f * TokyoRushGame.scale;
            //final float bulletSpeed = 130f * TokyoRushGame.scale;
            Vector2 playerPos = getPlayerCenterPosition();
            Bullet bullet = getFirstAvailableBullet();
            if (bullet != null) {
                bullet.set(Bullet.playerBullet, playerPos.x + 10f * TokyoRushGame.scale, playerPos.y, 0f, bulletSpeed, 0f);

                bullet = getFirstAvailableBullet();
                if (bullet != null) {
                    bullet.set(Bullet.playerBullet, playerPos.x - 10f * TokyoRushGame.scale, playerPos.y, 0f, bulletSpeed, 0f);
                }
            }
            fireCounter = 0;
        }
    }

    private Explosion getExplosion() {
        Explosion explosion;
        for(int i = 0; i < explosionCount; ++i) {
            explosion = explosions.get(i);
            if (!explosion.live) {
                return explosion;
            }
        }
        return null;
    }

    private void checkCollision() {
        Circle playerBulletCircle = new Circle();
        playerBulletCircle.radius = playerBulletRadius;

        for(Bullet bullet: playerBullets) {
            if (bullet.live) {
                playerBulletCircle.x = bullet.pos.x;
                playerBulletCircle.y = bullet.pos.y;

                for(Tank tank: tanks) {
                    if (tank.checkCollision(playerBulletCircle)) {
                        if (tank.damage(bullet.hitPoint)) {
                            Explosion explosion = getExplosion();
                            if (explosion != null) {
                                explosion.init(tank.pos);
                            }
                        }
                        bullet.live = false;
                    }
                }
            }
        }
    }

    @Override
    public void render() {
        beginRender();

        terrain.draw(batch, offset);

        for (Tank tank : tanks) {
            tank.draw(batch, offset);
        }

        for (Plant plant : plants) {
            plant.draw(batch, offset);
        }

        for (Bullet bullet : playerBullets) {
            if (bullet.live) {
                bullet.draw(batch, offset);
            }
        }

        for(Explosion explosion: explosions) {
            if (explosion.live) {
                explosion.draw(batch, offset);
            }
        }

        TokyoRushGame.player.render(batch);

        clouds.draw(batch, offset);

        endRender();

        //drawTankBoundingCircles();
        //drawBulletBoundingCircles();
    }

    private void drawBulletBoundingCircles() {
        Circle boundingCircle = new Circle();
        boundingCircle.radius = playerBulletRadius;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        for(Bullet bullet: playerBullets) {
            if (bullet.live) {
                boundingCircle.x = bullet.pos.x;
                boundingCircle.y = bullet.pos.y;
                shapeRenderer.circle(boundingCircle.x, boundingCircle.y, boundingCircle.radius);
            }
        }
        shapeRenderer.end();
    }

    private void drawTankBoundingCircles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Tank tank: tanks) {
            shapeRenderer.circle(tank.boundingCircle.x, tank.boundingCircle.y, tank.boundingCircle.radius);
        }
        shapeRenderer.end();
    }

    @Override
    public void touchDown(Vector3 position) {
        OrthographicCamera camera = TokyoRushGame.camera;

        TokyoRushGame.player.setTargetPosition(position.x, position.y);

        if (position.y > camera.viewportHeight - 100 * TokyoRushGame.scale) {
            TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.MAIN_MENU);
        }
    }

    @Override
    public void touchMove(Vector3 position) {
        //System.out.println("touchMove: x:" + position.x + ", y:" + position.y);
        TokyoRushGame.player.setTargetPosition(position.x, position.y);
    }
}

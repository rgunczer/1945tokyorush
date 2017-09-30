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

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    float playerBulletRadius;
    final int playerBulletCount = 120;

    float enemyBulletRadius;
    final int enemyBulletCount = 120;

    Terrain terrain;

    Array<Bullet> playerBullets;
    Array<Bullet> enemyBullets;

    Array<Plant> plants;
    Array<Tank> tanks;
    Array<Airplane> airplanes;
    Array<Explosion> explosions;

    final int plantCount = 33;
    final int tankCount = 9;
    final int airplaneCount = 6;
    final int explosionCount = 33;

    PlantFactory plantFactory;
    TankFactory tankFactory;
    AirplaneFactory airplaneFactory;

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

        enemyBullets = new Array<Bullet>(enemyBulletCount);
        for (int i = 0; i < enemyBulletCount; ++i) {
            enemyBullets.add(new Bullet());
        }

        plantFactory = new PlantFactory();
        plantFactory.create();

        tankFactory = new TankFactory();
        tankFactory.create();

        airplaneFactory = new AirplaneFactory();
        airplaneFactory.create();

        plants = new Array<Plant>(plantCount);
        tanks = new Array<Tank>(tankCount);
        explosions = new Array<Explosion>(explosionCount);
        airplanes = new Array<Airplane>(airplaneCount);

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
        enemyBulletRadius = 10f * TokyoRushGame.scale;

        offset.set(0f, 0f);
        scrollSpeedY = -1.2f;
        //scrollSpeedY = -0.2f;

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
        createRandomAirplanes();
    }

    private void createRandomAirplanes() {
        Airplane airplane;
        for (int i = 0; i < airplaneCount; ++i) {
            airplane = airplaneFactory.get(Airplane.ZERO_GREEN);
            airplane.pos.x = TokyoRushGame.camera.viewportWidth * 0.5f;
            airplane.pos.y = TokyoRushGame.camera.viewportHeight * 0.75f;
            airplane.init();

            airplanes.add(airplane);
        }
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

    public void fireEnemy(Vector2 pos, Vector2 vel) {
        Bullet bullet = getFirstAvailableEnemyBullet();
        if (bullet != null) {
            bullet.set(Bullet.redBullet,
                    pos.x,
                    pos.y,
                    vel.x,
                    vel.y);
        }
    }

    @Override
    public void update(float delta) {
        OrthographicCamera camera = TokyoRushGame.camera;

        checkCollisionPlayerBulletsVsEnemy();
        checkCollisionEnemyBulletsVsPlayer();

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

        for(Airplane airplane: airplanes) {
            airplane.update(delta, scrollSpeedY);
        }

        for(Bullet bullet: playerBullets) {
            if (bullet.live) {
                bullet.update(delta, scrollSpeedY);
            }
        }

        for(Bullet bullet: enemyBullets) {
            if (bullet.live) {
                bullet.update(delta, scrollSpeedY);
            }
        }

        TokyoRushGame.player.update(delta);

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

    public Bullet getFirstAvailableBullet() {
        Bullet bullet;
        for(int i = 0; i < playerBulletCount; ++i) {
            bullet = playerBullets.get(i);
            if (!bullet.live) {
                return bullet;
            }
        }
        return null;
    }

    public Bullet getFirstAvailableEnemyBullet() {
        Bullet bullet;
        for(int i = 0; i < enemyBulletCount; ++i) {
            bullet = enemyBullets.get(i);
            if (!bullet.live) {
                return bullet;
            }
        }
        return null;
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

    private void checkCollisionPlayerBulletsVsEnemy() {
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

    public void checkCollisionEnemyBulletsVsPlayer() {
        Circle enemyBulletCircle = new Circle();
        enemyBulletCircle.radius = 10f;

        for(Bullet bullet: enemyBullets) {
            if (bullet.live) {
                enemyBulletCircle.x = bullet.pos.x;
                enemyBulletCircle.y = bullet.pos.y;
                if (TokyoRushGame.player.checkCollision(enemyBulletCircle)) {
                    bullet.live = false;
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

        for (Airplane airplane: airplanes) {
            airplane.draw(batch, offset);
        }

        for(Bullet bullet: enemyBullets) {
            if (bullet.live) {
                bullet.draw(batch, offset);
            }
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

        drawTankBoundingCircles();
        //drawBulletBoundingCircles();
        drawPlayerBoundingCircle();
        drawEnemyBulletBoundingCircle();
    }

    private void drawEnemyBulletBoundingCircle() {
        Circle boundingCircle = new Circle();
        boundingCircle.radius = enemyBulletRadius;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        for(Bullet bullet: enemyBullets) {
            if (bullet.live) {
                boundingCircle.x = bullet.pos.x;
                boundingCircle.y = bullet.pos.y;
                shapeRenderer.circle(boundingCircle.x, boundingCircle.y, boundingCircle.radius);
            }
        }
        shapeRenderer.end();
    }

    private void drawPlayerBoundingCircle() {
        Circle boundingCircle = new Circle();
        boundingCircle.radius = TokyoRushGame.player.boundingCircleRadius;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);

        boundingCircle.x = TokyoRushGame.player.boundingCircle.x;
        boundingCircle.y = TokyoRushGame.player.boundingCircle.y;
        shapeRenderer.circle(boundingCircle.x, boundingCircle.y, boundingCircle.radius);

        shapeRenderer.end();
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
        for(Tank tank: tanks) {
            shapeRenderer.setColor( tank.getCanFire() ? Color.WHITE : Color.BLACK);
            shapeRenderer.circle(tank.boundingCircle.x, tank.boundingCircle.y, tank.boundingCircle.radius);
        }
        shapeRenderer.end();
    }

    @Override
    public void touchDown(Vector3 position) {
        OrthographicCamera camera = TokyoRushGame.camera;

        TokyoRushGame.player.setTargetPosition(position.x, position.y);

        if (position.y > camera.viewportHeight - 100 * TokyoRushGame.scale) {
            if (position.x > TokyoRushGame.camera.viewportWidth / 2) {
                TokyoRushGame.showScreen(TokyoRushGame.ScreenEnum.MAIN_MENU);
            } else {
                if (TokyoRushGame.player.gunType == Player.GunTypeEnum.DOUBLE_GUN) {
                    TokyoRushGame.player.gunType = Player.GunTypeEnum.TRIPLE_GUN;
                } else if (TokyoRushGame.player.gunType == Player.GunTypeEnum.TRIPLE_GUN) {
                    TokyoRushGame.player.gunType = Player.GunTypeEnum.QUAD_GUN;
                } else if (TokyoRushGame.player.gunType == Player.GunTypeEnum.QUAD_GUN) {
                    TokyoRushGame.player.gunType = Player.GunTypeEnum.DOUBLE_GUN;
                }
            }
        }
    }

    @Override
    public void touchMove(Vector3 position) {
        //System.out.println("touchMove: x:" + position.x + ", y:" + position.y);
        TokyoRushGame.player.setTargetPosition(position.x, position.y);
    }
}

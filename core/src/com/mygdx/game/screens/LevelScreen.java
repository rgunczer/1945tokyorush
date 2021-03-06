package com.mygdx.game.screens;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Airplane;
import com.mygdx.game.AirplaneFactory;
import com.mygdx.game.Bullet;
import com.mygdx.game.Clouds;
import com.mygdx.game.Explosion;
import com.mygdx.game.LevelLoader;
import com.mygdx.game.Plant;
import com.mygdx.game.PlantFactory;
import com.mygdx.game.Player;
import com.mygdx.game.Tank;
import com.mygdx.game.TankFactory;
import com.mygdx.game.Terrain;
import com.mygdx.game.TokyoRushGame;

public class LevelScreen extends BaseScreen {

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

    final int plantCount = 66;
    final int tankCount = 9;
    final int airplaneCount = 6;
    final int explosionCount = 33;

    PlantFactory plantFactory;
    TankFactory tankFactory;
    AirplaneFactory airplaneFactory;

    Vector2 offset;
    float scrollSpeedY;
    Vector2 worldPos;

    Clouds clouds;

    LevelLoader levelLoader;

    @Override
    public void create() {
        levelLoader = new LevelLoader();

        offset = new Vector2();
        worldPos = new Vector2();

        terrain = new Terrain();
        terrain.create();

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

        worldPos.setZero();

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

        levelLoader.load("jungle_strike.json");

        String name = levelLoader.levelInfo.name;
        System.out.println("level name: " + name);

        terrain.init(levelLoader.levelInfo.terrain);

        fillPlantsPool();
        createRandomTanks();
        createRandomExplosions();
        createRandomClouds();
        createRandomAirplanes();
    }

    private Plant getFreePlant() {
        for(int i = 0; i < plants.size; ++i) {
            if (!plants.get(i).live) {
                return plants.get(i);
            }
        }
        return null;
    }

    private Vector2 getRandomAirplanePosition() {
        Vector2 pos = new Vector2();
        pos.x = MathUtils.random(0f, TokyoRushGame.camera.viewportWidth);
        pos.y = TokyoRushGame.camera.viewportHeight * 1.1f;
        return pos;
    }

    private void createRandomAirplanes() {
        Airplane airplane;
        for (int i = 0; i < airplaneCount; ++i) {
            float speed = MathUtils.random(60f, 120f);
            int type = MathUtils.random(0, 3);
            airplane = airplaneFactory.get(type);
            airplane.pos.set(getRandomAirplanePosition());
            airplane.init(speed);

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

    private void fillPlantsPool() {
        OrthographicCamera camera = TokyoRushGame.camera;

        Plant template;
        Plant plant;
        for(int i = 0; i < plantCount; ++i) {
            template = plantFactory.get(MathUtils.random(0, 3));
            plant = new Plant(template);
            plant.pos.x = 0f; //MathUtils.random(-camera.viewportWidth / 4f, camera.viewportWidth);
            plant.pos.y = 0f; //MathUtils.random(0f, camera.viewportHeight);
            plant.rot = 0f; //MathUtils.random(0f, 360f);
            plant.scale = 1f; //MathUtils.random(0.6f, 1.1f);
            plant.live = false;
            plants.add(plant);
        }
    }

    private void createRandomTanks() {
        OrthographicCamera camera = TokyoRushGame.camera;

        Tank tank;
        for (int i = 0; i < tankCount; ++i) {
            if (MathUtils.randomBoolean()) {
                tank = tankFactory.get(Tank.TANK_TYPE_SMALL);
            } else {
                tank = tankFactory.get(Tank.TANK_TYPE_BIG);
            }
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

    public void getItemsToLevel(float bottomY, float topY) {
        LevelLoader.PlantInfo plantInfo;
        for(int i = levelLoader.plantIndex; i < levelLoader.plants.size; ++i) {
            plantInfo = levelLoader.plants.get(i);
            if (plantInfo.pos.y < topY) {
                Plant plant = getFreePlant();
                if (plant != null) {
                    plant.live = true;
                    plant.pos.x = plantInfo.pos.x;
                    plant.pos.y = plantInfo.pos.y - bottomY;

                    Plant template = plantFactory.get(plantInfo.type);

                    if (plantInfo.type.startsWith("bush")) {
                        plant.scale = 0.5f; //MathUtils.random(0.5f, 0.6f);
                        plant.rot = 0f; //MathUtils.random(0f, 360f);
                    } else {
                        plant.scale = MathUtils.random(0.8f, 1.1f);
                        plant.rot = MathUtils.random(0f, 360f);
                    }

                    if (template != null) {
                        plant.setApperance(template);
                    } else {
                        System.out.println("Plant template NOT found");
                    }
                } else {
                    levelLoader.plantIndex = i;
                    return;
                }
            } else {
                levelLoader.plantIndex = i;
                return;
            }
        }
    }

    @Override
    public void update(float delta) {
        OrthographicCamera camera = TokyoRushGame.camera;
        worldPos.y += Math.abs(scrollSpeedY);

        getItemsToLevel(worldPos.y, worldPos.y + camera.viewportHeight);

        checkCollisionPlayerBulletsVsEnemy();
        checkCollisionEnemyBulletsVsPlayer();

        terrain.update(scrollSpeedY);

        for(Plant plant: plants) {
            if (plant.live) {
                plant.update(scrollSpeedY);

                if (plant.pos.y < -camera.viewportWidth / 3f) {
                    //putPlantToRandomPosition(plant);
                    plant.live = false;
                }
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

            if (airplane.pos.y < -TokyoRushGame.camera.viewportHeight * 0.1f ||
                airplane.pos.y > (TokyoRushGame.camera.viewportHeight + TokyoRushGame.camera.viewportWidth) ||
                airplane.pos.x < -TokyoRushGame.camera.viewportHeight * 0.1f ||
                airplane.pos.x > TokyoRushGame.camera.viewportWidth * 1.1f || airplane.dead) {

                if (airplane.dead) {
                    spawnExplosionAt(airplane.pos);
                }
                airplane.pos.set(getRandomAirplanePosition());
                airplane.init(MathUtils.random(120f, 240f));
            }
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

    private void spawnExplosionAt(Vector2 pos) {
        Explosion explosion = getExplosion();
        if (explosion != null) {
            explosion.init(pos);
        }
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
                            spawnExplosionAt(tank.pos);
                        }
                        bullet.live = false;
                    }
                }

                if (bullet.live) {
                    for(Airplane airplane: airplanes) {
                        if (airplane.checkCollision(playerBulletCircle)) {
                            if (airplane.damage(bullet.hitPoint)) {
                                spawnExplosionAt(airplane.pos);
                            }
                            bullet.live = false;
                        }
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
    public void render(float dt) {
        beginRender();

        terrain.draw(batch, offset);

        for (Tank tank : tanks) {
            tank.draw(batch, offset);
        }

        for (Plant plant : plants) {
            if (plant.live) {
                plant.draw(batch, offset);
            }
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

        //drawTankBoundingCircles();
        //drawBulletBoundingCircles();
        //drawPlayerBoundingCircle();
        //drawEnemyBulletBoundingCircle();
        //drawAirplaneBoundingCircles();
    }

    private void drawAirplaneBoundingCircles() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        for(Airplane airplane: airplanes) {
            //shapeRenderer.setColor( tank.getCanFire() ? Color.WHITE : Color.BLACK);
            shapeRenderer.circle(airplane.boundingCircle.x, airplane.boundingCircle.y, airplane.boundingCircle.radius);
        }
        shapeRenderer.end();
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

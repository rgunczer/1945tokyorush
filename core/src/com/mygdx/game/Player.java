package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;


public class Player {

    public enum PlayerStateEnum {
        IDLE_ON_AIRFIELD,
        FLYING,
        TAKEOFF,
    }

    public enum GunTypeEnum {
        DOUBLE_GUN,
        TRIPLE_GUN,
        QUAD_GUN
    }

    Texture texture;
    TextureRegion hit;
    TextureRegion normal;
    TextureRegion shadow;
    TextureRegion body;

    float bulletSpeed;
    float hitCooldown;
    float takeOffSpeed = 100f;
    public float boundingCircleRadius;
    float distanceToTarget;

    float fingerOffsetY;

    public  Circle boundingCircle;

    public GunTypeEnum gunType;

    Animation<TextureRegion> propellerAnim;

    Vector2 posTakeOff = new Vector2();

    Vector2 target = new Vector2();
    Vector2 diff = new Vector2();
    public Vector2 pos = new Vector2();
    Vector2 posTarget = new Vector2();
    Vector2 velocity = new Vector2();

    float speed;

    float propAnimStartTime;

    int fireCounter;

    public float shadowDistance;
    public float scaleShadow;

    public PlayerStateEnum state;

    public float scale = 1f;


    public void create() {
        state = PlayerStateEnum.IDLE_ON_AIRFIELD;
        final String textureName = "player-and-propeller.png";

        texture = TextureFactory.create(textureName);
        hit = new TextureRegion(texture, 0, 0, 168, 168);
        normal = new TextureRegion(texture, 168, 0, 168, 168);
        shadow = new TextureRegion(texture, 336, 0, 168, 168);


        TextureRegion[] propFrames = new TextureRegion[3];
        propFrames[0] = new TextureRegion(texture, 0, 168, 168, 168);
        propFrames[1] = new TextureRegion(texture, 168, 168, 168, 168);
        propFrames[2] = new TextureRegion(texture, 336, 168, 168, 168);

        propellerAnim = new Animation<TextureRegion>(0.2f, propFrames);
        propellerAnim.setPlayMode(Animation.PlayMode.LOOP);

        OrthographicCamera camera = TokyoRushGame.camera;

        fingerOffsetY = camera.viewportWidth / 6f;

        float w = normal.getRegionWidth();
        float h = normal.getRegionHeight();
        float x = camera.viewportWidth * 0.5f;
        float y = camera.viewportWidth / 4.0f;

        pos.x = x;
        pos.y = y;

        posTakeOff.x = pos.x;
        posTakeOff.y = pos.y;

        gunType = GunTypeEnum.DOUBLE_GUN;

        bulletSpeed = 730f * TokyoRushGame.scale;
        boundingCircleRadius = 40f * TokyoRushGame.scale;

        boundingCircle = new Circle();
        boundingCircle.radius = boundingCircleRadius;
    }

    public void setOnAirfield() {
        propAnimStartTime = 0f;
        pos.set(posTakeOff);

        takeOffSpeed = 400f;
        scale = 0.75f;
        shadowDistance = 5f * TokyoRushGame.scale;
        scaleShadow = scale * 0.95f;
        state = PlayerStateEnum.IDLE_ON_AIRFIELD;
        body = normal;
        hitCooldown = 0.4f;
    }

    public void setOnLevel() {
        fireCounter = 0;
        propAnimStartTime = 0f;
        state = PlayerStateEnum.FLYING;
        OrthographicCamera camera = TokyoRushGame.camera;
        pos.x = camera.viewportWidth * 0.5f;
        pos.y = camera.viewportWidth / 3.0f;

        posTarget.set(pos);

        speed = 20.0f * TokyoRushGame.scale;

        scale = 1.0f;
        shadowDistance = 60f * TokyoRushGame.scale;
        scaleShadow = 0.5f;
        body = normal;
    }

    public void setTargetPosition(float x, float y) {
        target.set(x, y + fingerOffsetY);

        diff.x = target.x - pos.x;
        diff.y = target.y - pos.y;

        float toTarget = target.dst(pos) * 1.01f;

        float len = diff.len();
        if (len > speed * TokyoRushGame.scale) {
            diff.nor();
            velocity.set(diff);
            velocity.scl(speed);
            posTarget.set(target);
            distanceToTarget = toTarget;
        }
    }

    public Vector2 getCenterPosition() {
        Vector2 p = new Vector2(pos);
        float width = normal.getRegionWidth() * TokyoRushGame.scale;
        float height = normal.getRegionHeight() * TokyoRushGame.scale;

        float originX = width * 0.5f;
        float originY = height * 0.5f;

        p.x = p.x - (width * 0.5f) + originX;
        p.y = p.y - (height * 0.5f) + originY;

        return p;
    }

    public boolean checkCollision(Circle circle) {
        if (state == PlayerStateEnum.FLYING) {
            boolean hit = boundingCircle.overlaps(circle);
            if (hit) {
                //body = template.bodyHit;
                hitCooldown = 0.4f;
                body = this.hit;
            }
            return hit;
        }
        return false;
    }


    public void update(float delta) {
        propAnimStartTime += 0.1f;
        switch (state) {
            case TAKEOFF:
                pos.y += takeOffSpeed * delta;
                takeOffSpeed += 10f;

                if (pos.y > TokyoRushGame.camera.viewportHeight / 3f) {
                    shadowDistance += 30f * delta;
                    scaleShadow *= 0.995f;
                }

                break;

            case FLYING: {
                float distance = posTarget.dst(pos);
                //System.out.println("distance: " + distance);
                if (distance < distanceToTarget) {
                    float x = velocity.x * speed * delta;
                    float y = velocity.y * speed * delta;
                    pos.add(x, y);
                    distanceToTarget = distance;
                }
                fire();
            }
            break;
        }

        if (body == hit) {
            hitCooldown -= 0.1f;
            if (hitCooldown < 0f) {
                body = normal;
            }
        }
    }

    private Bullet getFreeBullet() {
        return TokyoRushGame.levelScreen.getFirstAvailableBullet();
    }

    public void fire() {
        if (++fireCounter > 10) {
            switch (gunType) {
                case DOUBLE_GUN:
                    fireDoubleGun();
                    break;

                case TRIPLE_GUN:
                    fireTripleGun();
                    break;

                case QUAD_GUN:
                    fireQuadGun();
                    break;
            }
            fireCounter = 0;
        }
    }

    private void fireDoubleGun() {
        Vector2 playerPos = getCenterPosition();
        Bullet bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x - 10f * TokyoRushGame.scale,
                    playerPos.y,
                    0f,
                    bulletSpeed);
        }

        bullet = TokyoRushGame.levelScreen.getFirstAvailableBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x + 10f * TokyoRushGame.scale,
                    playerPos.y,
                    0f,
                    bulletSpeed);
        }
    }

    private void fireTripleGun() {
        Vector2 playerPos = getCenterPosition();
        Bullet bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x,
                    playerPos.y,
                    bulletSpeed * 0.1f,
                    bulletSpeed);
        }

        bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x,
                    playerPos.y,
                    bulletSpeed * 0f,
                    bulletSpeed * 1.01f);
        }

        bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x,
                    playerPos.y,
                    bulletSpeed * -0.1f,
                    bulletSpeed);
        }
    }

    private void fireQuadGun() {
        Vector2 playerPos = getCenterPosition();
        Bullet bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x - 30f * TokyoRushGame.scale,
                    playerPos.y,
                    0f,
                    bulletSpeed);
        }

        bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x - 10f * TokyoRushGame.scale,
                    playerPos.y + 10f * TokyoRushGame.scale,
                    0f,
                    bulletSpeed);
        }

        bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x + 10f * TokyoRushGame.scale,
                    playerPos.y + 10f * TokyoRushGame.scale,
                    0f,
                    bulletSpeed);
        }

        bullet = getFreeBullet();
        if (bullet != null) {
            bullet.set(Bullet.playerBullet,
                    playerPos.x + 30f * TokyoRushGame.scale,
                    playerPos.y,
                    0f,
                    bulletSpeed);
        }
    }

    public void render(SpriteBatch batch) {
        float width = normal.getRegionWidth() * TokyoRushGame.scale;
        float height = normal.getRegionHeight() * TokyoRushGame.scale;

        float originX = width * 0.5f;
        float originY = height * 0.5f;

        final float rotation = 0f;

        float x = pos.x - width * 0.5f;
        float y = pos.y - height * 0.5f;

        batch.draw(shadow, x + shadowDistance, y - shadowDistance, originX, originY, width, height, scaleShadow, scaleShadow, rotation);
        batch.draw(body, x, y, originX, originY, width, height, scale, scale, rotation);

        TextureRegion prop = propellerAnim.getKeyFrame(propAnimStartTime, true);
        batch.draw(prop, x, y, originX, originY, width, height, scale, scale, rotation);

        boundingCircle.x = pos.x;
        boundingCircle.y = pos.y - (10f * TokyoRushGame.scale);
    }
}

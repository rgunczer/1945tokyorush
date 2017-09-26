package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class Tank {

    public enum TankState {
        IDLE,
        MOVING,
        SEEK,
        TAKING_DAMAGE,
        DESTROYED
    }

    public static int TANK_TYPE_SMALL = 0;
    public static int TANK_TYPE_BIG = 1;

    TextureRegion body;
    TextureRegion shadow;

    float rot;
    Vector2 pos;
    Vector2 vel;
    float scale;
    int hitPoint;

    float hitCooldown;

    TankTemplate template;

    Circle boundingCircle;

    public TankState state;

    int type;

    public Turret turret;

    public Tank(TankTemplate template) {
        this.template = template;
        this.type = template.type;
        this.body = template.bodyNormal;
        this.shadow = template.bodyShadow;
        this.pos = new Vector2();
        this.scale = template.scale;
        this.boundingCircle = new Circle(template.boundingCircle);
        this.hitPoint = template.hitPoint;
    }

    public void addTurret(Turret turret) {
        this.turret = turret;
    }

    public void init() {
        hitPoint = template.hitPoint;
        //state = TankState.IDLE;
        rot = MathUtils.random(0f, 360f);
        vel = getRandomTankVelocity(rot);

        body = template.bodyNormal;
        shadow = template.bodyShadow;
        turret.showNormal();
        turret.rot = rot;
    }

    public boolean getCanFire() {
        return turret.canFire;
    }

    private Vector2 getRandomTankVelocity(float rot) {
        Vector2 vel = new Vector2(0f, 10f);
        vel.rotate( rot );

        return vel;
    }

    public void update(float delta, float scrollY) {
        pos.y += scrollY;

        pos.x += vel.x * delta;
        pos.y += vel.y * delta;

        turret.update(delta, pos);

        if (body == template.bodyHit) {
            hitCooldown -= 0.1f;
            if (hitCooldown < 0f) {
                body = template.bodyNormal;
                turret.showNormal();
            }
        }
    }

    public boolean checkCollision(Circle circle) {
        if (body != template.bodyWreck) {
            boolean hit = boundingCircle.overlaps(circle);
            if (hit) {
                body = template.bodyHit;
                hitCooldown = 0.4f;
                turret.showHit();
            }
            return hit;
        }
        return false;
    }

    public boolean damage(int hitPoint) {
        this.hitPoint -= hitPoint;
        if (this.hitPoint <= 0) {
            this.vel.setZero();
            turret.showWreck();
            body = template.bodyWreck;
            shadow = template.bodyWreckShadow;
            return true;
        }
        return false;
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        float w = body.getRegionWidth() * TokyoRushGame.scale;
        float h = body.getRegionHeight() * TokyoRushGame.scale;
        float originX = w * 0.5f;
        float originY = h * 0.5f;
        float scaleX = scale;
        float scaleY = scale;

        float x = pos.x - w * 0.5f;
        float y = pos.y - h * 0.5f;
        float shadowOffset = 6f * TokyoRushGame.scale;

        batch.draw(shadow, x + offset.x + shadowOffset, y + offset.y - shadowOffset, originX, originY, w, h, scaleX, scaleY, rot);
        batch.draw(body, x + offset.x, y + offset.y, originX, originY, w, h, scaleX, scaleY, rot);

        //turret.draw(batch, new Vector2(pos.x, pos.y), offset);
        turret.draw(batch, offset);

        boundingCircle.x = pos.x;
        boundingCircle.y = pos.y;
    }

}

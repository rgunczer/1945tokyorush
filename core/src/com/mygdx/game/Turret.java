package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret {

    public static int TURRET_TANK_SMALL = 0;
    public static int TURRET_TANK_BIG = 1;

    TextureRegion body;
    float scale;
    float rot;
    Vector2 offsetParent;
    Vector2 parentPos;
    Vector2 pos;
    TurretTemplate template;
    int turretCooldown;

    private float width;
    private float height;
    private float originX;
    private float originY;

    public Turret(TurretTemplate template) {
        this.template = template;
        this.offsetParent = template.parentOffset;
        this.body = template.turretNormal;
        this.scale = template.scale;
        this.parentPos = new Vector2();

        width = body.getRegionWidth() * TokyoRushGame.scale;
        height = body.getRegionHeight() * TokyoRushGame.scale;
        originX = width * 0.5f;
        originY = height * 0.5f;

        pos = new Vector2();
        turretCooldown = 100;
    }

    public void showHit() {
        this.body = template.turretHit;
    }

    public void showNormal() {
        this.body = template.turretNormal;
    }

    public void showWreck() {
        this.body = template.turretWreck;
    }

    public void update(float delta, Vector2 parentPos) {
        this.parentPos.set(parentPos);

        if (this.body == template.turretNormal) {
            seekTarget(delta);
            //rot += 0.5f;
        }
    }

    private void calcTurretPos() {
        pos.x = parentPos.x + offsetParent.x;
        pos.y = parentPos.y + offsetParent.y;
    }

    private void seekTarget(float delta) {
        --turretCooldown;
        boolean canFire = false;
        final float angularVelocity = 0.01f;
        Vector2 playerPos = TokyoRushGame.player.getCenterPosition();
        calcTurretPos();

        rot += 90f;

        float angle = MathUtils.degreesToRadians * rot;

        float desiredAngle = MathUtils.atan2(playerPos.y - pos.y, playerPos.x - pos.x);
        float angleDiff = desiredAngle - angle;

        if ( Math.abs(MathUtils.radiansToDegrees * angleDiff) < 1.0f ) {
            canFire = true;
        }

        // Normalize angle to [-PI,PI] range. This ensures that the turret turns the shortest way.
        while (angleDiff < -MathUtils.PI) angleDiff += 2 * MathUtils.PI;
        while (angleDiff >= MathUtils.PI) angleDiff -= 2 * MathUtils.PI;

        if (angleDiff > 0.0f)
            angle += angularVelocity;

        if (angleDiff < 0.0f)
            angle -= angularVelocity;

        rot = MathUtils.radiansToDegrees * angle;
        rot -= 90f;

        if (canFire && turretCooldown < 0) {
            Vector2 gunEnd = new Vector2(0f, 35f * TokyoRushGame.scale);
            gunEnd.rotate(rot);
            Vector2 vel = new Vector2(playerPos.x - pos.x, playerPos.y - pos.y);
            vel.nor();
            vel.scl(200f * TokyoRushGame.scale);
            TokyoRushGame.levelScreen.fireEnemy( new Vector2(pos.x + gunEnd.x, pos.y + gunEnd.y), vel);
            turretCooldown = 100;
        }

/*
        angle = TO_RAD(rotation);

        _sin_radian = sin(angle);
        _cos_radian = cos(angle);

        m_positionEnd.x = position.x - m_gun_radius * _sin_radian;
        m_positionEnd.y = position.y + m_gun_radius * _cos_radian;

        m_time_elapsed += dt;

        if (m_time_elapsed > 1.0f)
        {
            m_time_elapsed = 0.0f;

            if ( abs(TO_DEG(angleDiff)) < 1.0f )
            {
                Fire();
            }
        }
*/
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        calcTurretPos();

        float x = pos.x - width * 0.5f;
        float y = pos.y - height * 0.5f;

        batch.draw(body, x + offset.x, y + offset.y, originX, originY, width, height, scale, scale, rot);
    }

}

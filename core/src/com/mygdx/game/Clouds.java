package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Clouds {

    private static Texture texture;

    private static TextureRegion cloud0;
    private static TextureRegion cloud1;

    private class CloudInfo {
        TextureRegion body;
        Vector2 pos;
        float rot;
        float scale;
    }

    private static int cloudCount = 9;

    private Array<CloudInfo> cloudInfos;

    public static void create() {
        texture = new Texture("clouds.png");
        cloud0 = new TextureRegion(texture, 0, 0, 512, 512);
        cloud1 = new TextureRegion(texture, 512, 0, 512, 512);
    }

    private TextureRegion getRandomCloudFrame() {
        final int cloudTypeCount = 2;
        int randomNumber = MathUtils.random(0, cloudTypeCount - 1);

        if (randomNumber == 0) {
            return cloud0;
        } else if (randomNumber == 1) {
            return cloud1;
        } else {
            System.out.println("oops! getRandomCloudFrame");
        }
        return null;
    }

    public void init() {
        CloudInfo cloudInfo;
        cloudInfos = new Array<CloudInfo>(cloudCount);
        for (int i = 0; i < cloudCount; ++i) {
            cloudInfo = new CloudInfo();
            cloudInfo.body = getRandomCloudFrame();
            cloudInfo.pos = new Vector2();
            cloudInfo.pos.x = MathUtils.random(0f, TokyoRushGame.camera.viewportWidth);
            cloudInfo.pos.y = MathUtils.random(0f, TokyoRushGame.camera.viewportHeight);
            cloudInfo.rot = MathUtils.random(-10f, 10f);
            cloudInfo.scale = MathUtils.random(0.75f, 1.25f);
            cloudInfos.add(cloudInfo);
        }
    }

    public void update(float delta, float scrollY) {
        float cloudWidth = cloudInfos.get(0).body.getRegionWidth() * TokyoRushGame.scale;
        float cloudHeight = cloudInfos.get(0).body.getRegionHeight() * TokyoRushGame.scale;

        for(CloudInfo cloudInfo: cloudInfos) {
            cloudInfo.pos.y += scrollY;

            if (cloudInfo.pos.y < -cloudHeight * 0.5f) {
                cloudInfo.pos.x = MathUtils.random(-cloudWidth, TokyoRushGame.camera.viewportWidth + cloudWidth);
                cloudInfo.pos.y = MathUtils.random(TokyoRushGame.camera.viewportHeight + cloudHeight * 0.5f, TokyoRushGame.camera.viewportHeight + cloudHeight);
                cloudInfo.scale = MathUtils.random(0.75f, 1.25f);
                cloudInfo.rot = MathUtils.random(-10f, 10f);
            }
        }
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        for(CloudInfo cloudInfo: cloudInfos) {
            float rot = cloudInfo.rot;
            float scale = cloudInfo.scale;

            float w = cloudInfo.body.getRegionWidth() * TokyoRushGame.scale;
            float h = cloudInfo.body.getRegionHeight() * TokyoRushGame.scale;

            float originX = w * 0.5f;
            float originY = h * 0.5f;

            float x = cloudInfo.pos.x - w * 0.5f;
            float y = cloudInfo.pos.y - h * 0.5f;

            batch.draw(cloudInfo.body, x + offset.x, y + offset.y, originX, originY, w, h, scale, scale, rot);
        }
    }

}

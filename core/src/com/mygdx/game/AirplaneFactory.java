package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AirplaneFactory {

    Texture texture;

    final int templatesCount = 4;
    Array<AirplaneTemplate> templates;

    TextureRegion shadow;

    TextureRegion panther;
    TextureRegion pantherHit;

    TextureRegion green;
    TextureRegion greenHit;

    TextureRegion black;
    TextureRegion blackHit;

    TextureRegion white;
    TextureRegion whiteHit;

    Animation<TextureRegion> propellerAnim;

    float boundingCircleRadius;

    public void create() {
        texture = new Texture("airplanes.png");

        boundingCircleRadius = 30f * TokyoRushGame.scale;

        final int sc = 2;

        shadow = new TextureRegion(texture, 0, 0, 60*sc, 60*sc);

        panther = new TextureRegion(texture, 60*sc, 0, 60*sc, 60*sc);
        pantherHit = new TextureRegion(texture, 60*sc, 60*sc, 60*sc, 60*sc);

        green = new TextureRegion(texture, 120*sc, 0, 60*sc, 60*sc);
        greenHit = new TextureRegion(texture, 120*sc, 60*sc, 60*sc, 60*sc);

        black = new TextureRegion(texture, 180*sc, 0, 60*sc, 60*sc);
        blackHit = new TextureRegion(texture, 180*sc, 60*sc, 60*sc, 60*sc);

        white = new TextureRegion(texture, 240*sc, 0, 60*sc, 60*sc);
        whiteHit = new TextureRegion (texture, 240*sc, 60*sc, 60*sc, 60*sc);

        TextureRegion[] propFrames = new TextureRegion[3];
        propFrames[0] = new TextureRegion(texture,   0, 240, 60*sc, 60*sc);
        propFrames[1] = new TextureRegion(texture, 120, 240, 60*sc, 60*sc);
        propFrames[2] = new TextureRegion(texture, 240, 240, 60*sc, 60*sc);

        propellerAnim = new Animation<TextureRegion>(0.2f, propFrames);
        propellerAnim.setPlayMode(Animation.PlayMode.LOOP);

        AirplaneTemplate.propellerAnim = propellerAnim;

        templates = new Array<AirplaneTemplate>(templatesCount);

        templates.add(createPanther());
        templates.add(createBlack());
        templates.add(createWhite());
        templates.add(createGreen());
    }

    private AirplaneTemplate createPanther() {
        AirplaneTemplate template = new AirplaneTemplate();
        template.body = panther;
        template.hit = pantherHit;
        template.shadow = shadow;
        template.boundingCircle = new Circle(new Vector2(0f, 0f), boundingCircleRadius);
        template.hitPoint = 10;
        template.scale = 0.7f;

        return template;
    }

    private AirplaneTemplate createGreen() {
        AirplaneTemplate template = new AirplaneTemplate();
        template.body = green;
        template.hit = greenHit;
        template.shadow = shadow;
        template.boundingCircle = new Circle(new Vector2(0f, 0f), boundingCircleRadius);
        template.hitPoint = 15;
        template.scale = 0.7f;

        return template;
    }

    private AirplaneTemplate createBlack() {
        AirplaneTemplate template = new AirplaneTemplate();
        template.body = black;
        template.hit = blackHit;
        template.shadow = shadow;
        template.boundingCircle = new Circle(new Vector2(0f, 0f), boundingCircleRadius);
        template.hitPoint = 30;
        template.scale = 0.7f;

        return template;
    }

    private AirplaneTemplate createWhite() {
        AirplaneTemplate template = new AirplaneTemplate();
        template.body = white;
        template.hit = whiteHit;
        template.shadow = shadow;
        template.boundingCircle = new Circle(new Vector2(0f, 0f), boundingCircleRadius);
        template.hitPoint = 20;
        template.scale = 0.7f;

        return template;
    }

    Airplane get(int type) {
        AirplaneTemplate template = null;

        if (type == Airplane.ZERO_PANTHER) {
            template = templates.get(type);
        } else if (type == Airplane.ZERO_BLACK) {
            template = templates.get(type);
        } else if (type == Airplane.ZERO_GREEN) {
            template = templates.get(type);
        } else if (type == Airplane.ZERO_WHITE) {
            template = templates.get(type);
        }

        if (template != null) {
            Airplane airplane = new Airplane(template);
            return airplane;
        }

        return null;
    }

}

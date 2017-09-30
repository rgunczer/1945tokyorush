package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AirplaneFactory {

    Texture texture;

    final int airplateTemplateCount = 4;
    Array<AirplaneTemplate> airplaneTemplates;

    TextureRegion shadow;

    TextureRegion panther;
    TextureRegion pantherHit;

    TextureRegion green;
    TextureRegion greenHit;

    TextureRegion black;
    TextureRegion blackHit;

    TextureRegion white;
    TextureRegion whiteHit;

    public void create() {
        texture = new Texture("airplanes.png");

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

        //"prop_small"=(22,54,17,17)
        //"prop_med"=(22,67,17,17)
        //"prop_big"=(22,82,17,17)

        airplaneTemplates = new Array<AirplaneTemplate>(airplateTemplateCount);
        AirplaneTemplate airplaneTemplate = createPanther();
        airplaneTemplates.add(airplaneTemplate);
    }

    private AirplaneTemplate createPanther() {
        AirplaneTemplate template = new AirplaneTemplate();
        template.body = panther;
        template.hit = pantherHit;
    }

}

package com.mygdx.game.menu;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.TextureFactory;
import com.mygdx.game.TokyoRushGame;

public class MenuTitle {

    Texture texture;
    Sprite sprite;
    MenuState state;

    private float posYready;
    private float posYoffscreen;
    private float progress;

    public MenuTitle(String textureName) {
        System.out.println("MenuTitle ctor");
        texture = TextureFactory.create(textureName);
        sprite = new Sprite(texture);
        state = MenuState.ANIM_APPEAR;
    }

    public void calcPositions() {
        OrthographicCamera camera = TokyoRushGame.camera;

        float scale = TokyoRushGame.scale;
        float w = TokyoRushGame.referenceWidth * scale;
        float h = TokyoRushGame.referenceHeight * scale;
        float y = camera.viewportHeight - sprite.getHeight() * scale;
        float x = 0f;

        sprite.setBounds(x, y, w, sprite.getHeight() * scale);

        posYready = y;

        posYoffscreen = posYready + sprite.getHeight();

        sprite.setY(posYoffscreen);
        progress = 0f;
    }

    public void setState(MenuState state) {
        this.state = state;
        progress = 0f;
        switch (state) {
            case OFFSCREEN:
                sprite.setY(posYoffscreen);
                break;

            case ANIM_APPEAR:
                break;

            case ANIM_DISAPPEAR:
                break;

            case READY:
                sprite.setY(posYready);
                break;
        }
    }

    public void update(float delta) {
        progress += delta * 2f;
        progress = MathUtils.clamp(progress, 0f, 1f);
        float y = sprite.getY();

        switch (state) {
            case ANIM_APPEAR:
                y = Interpolation.pow4.apply(posYoffscreen, posYready, progress);
                if (MathUtils.isEqual(progress, 1.0f)) {
                    state = MenuState.READY;
                }
                break;

            case ANIM_DISAPPEAR:
                y = Interpolation.pow4.apply(posYready, posYoffscreen, progress);
                if (MathUtils.isEqual(progress, 1.0f)) {
                    state = MenuState.OFFSCREEN;
                }
                break;

        }
        sprite.setY(y);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}

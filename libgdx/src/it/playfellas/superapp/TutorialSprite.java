package it.playfellas.superapp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class TutorialSprite extends SimpleSprite {

    private SimpleSprite bgSprite;
    private SimpleSprite tileSprite;
    private boolean pulse = false;


    public TutorialSprite(SimpleSprite bgSprite, SimpleSprite tileSprite) {
        this.bgSprite = bgSprite;
        this.tileSprite = tileSprite;
    }

    @Override
    public void incrementX(float increment) {
        bgSprite.incrementX(increment);
        if(pulse){
            float alpha = (float) (0.75 * (Math.sin(System.currentTimeMillis() * 0.005) + 1));
            bgSprite.setColor(1f, 1f, 1f, alpha > 1 ? 1 : alpha);
        }
        tileSprite.incrementX(increment);
    }

    @Override
    public float getWidth() {
        return bgSprite.getWidth();
    }

    @Override
    public float getHeight() {
        return bgSprite.getHeight();
    }

    @Override
    public void setPosition(float x, float y) {
        bgSprite.setPosition(x, y);
        float tileX = x + ((bgSprite.getWidth() - tileSprite.getWidth()) / 2);
        float tileY = y + ((bgSprite.getHeight() - tileSprite.getHeight()) / 2);
        tileSprite.setPosition(tileX, tileY);
    }

    @Override
    public void draw(Batch batch) {
        bgSprite.draw(batch);
        tileSprite.draw(batch);
    }

    @Override
    public Rectangle getBoundingRectangle() {
        return tileSprite.getBoundingRectangle();
    }

    @Override
    public void decreaseSize() {
        tileSprite.decreaseSize();
    }

    @Override
    public boolean isDead() {
        return tileSprite.getWidth() < 1;
    }

    public void setPulse(boolean pulse) {
        this.pulse = pulse;
    }

    @Override
    public void disposeTexture() {
        if (bgSprite.getTexture() != null) {
            bgSprite.getTexture().dispose();
        }
        if (tileSprite.getTexture() != null) {
            tileSprite.getTexture().dispose();
        }
    }
}

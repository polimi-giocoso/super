package it.playfellas.superapp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SimpleSprite extends Sprite {

    private boolean leaving = false;

    public SimpleSprite() {
    }

    public SimpleSprite(Texture texture) {
        super(texture);
    }

    public void incrementX(float increment) {
        setX(getX() + increment);
    }

    public void decreaseSize() {
        if (isDead()) {
            return;
        }

        float oldWidth = getWidth();
        float oldHeight = getHeight();
        float newWidth = oldWidth - 30;
        float newHeight = oldHeight - 30;
        setSize(newWidth, newHeight);
        setPosition(getX() + (oldWidth - newWidth) / 2, getY() + (oldHeight - newHeight) / 2);
    }

    public boolean isDead() {
        return getWidth() < 1;
    }

    public void setLeaving(boolean leaving) {
        this.leaving = leaving;
    }

    public boolean isLeaving() {
        return leaving;
    }

    public void disposeTexture() {
        if (getTexture() != null) {
            getTexture().dispose();
        }
    }
}

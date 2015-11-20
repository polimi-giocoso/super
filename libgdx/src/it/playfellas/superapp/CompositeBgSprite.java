package it.playfellas.superapp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class CompositeBgSprite extends SimpleSprite {

    private Array<SimpleSprite> sprites;

    public CompositeBgSprite() {
        sprites = new Array<SimpleSprite>();
    }

    public void addSprite(SimpleSprite sprite) {
        sprites.add(sprite);
    }

    public Array<SimpleSprite> getSprites() {
        return sprites;
    }

    public void setSprites(Array<SimpleSprite> sprites) {
        this.sprites = sprites;
    }

    @Override
    public void draw(Batch batch) {
        for (SimpleSprite sprite : sprites) {
            sprite.draw(batch);
        }
    }

    @Override
    public void disposeTexture() {
        for (SimpleSprite simpleSprite :
                sprites) {
            if (simpleSprite.getTexture() != null) {
                simpleSprite.getTexture().dispose();
            }
        }
    }
}

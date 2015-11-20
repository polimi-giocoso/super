package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import it.playfellas.superapp.CompositeBgSprite;
import it.playfellas.superapp.SimpleSprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;

public class SizeConveyor extends Conveyor {

    private int foundTiles = 0;

    public SizeConveyor(BaseListener listener) {
        super(listener);
    }

    @Override
    public void init(float h, float w, final float relativeY) {
        super.init(h, w, relativeY);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Texture bgTexture = new Texture("_slot.png");
                bgTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
                CompositeBgSprite compositeBgSprite = new CompositeBgSprite();
                for (int i = 0; i < 4; i++) {
                    SimpleSprite s = new SimpleSprite(bgTexture);
                    float slotSpace = (width / 4);
                    float x = (slotSpace * i) + (slotSpace - height) / 2;
                    s.setBounds(x, relativeY, height, height);
                    compositeBgSprite.addSprite(s);
                }
                setBgSprite(compositeBgSprite);
            }
        });
    }

    @Override
    public void update() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void addTile(Tile tile) {
    }

    @Override
    public void addTile(TutorialTile tile) {
    }

    @Override
    public void touch(Vector3 touchPos) {
    }

    @Override
    public void clear() {
        foundTiles = 0;
        super.clear();
    }

    public void addBaseTiles(final Tile[] tiles) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < tiles.length; i++) {
                    SimpleSprite tileSprite = makeSprite(tiles[i]);
                    tileSprite.setColor(Color.BLACK);
                    tileSprite.setPosition(calculateSpriteX(tileSprite, tiles.length, i), calculateSpriteY(tileSprite));
                    TileRepr tileRepr = new TileRepr(tileSprite, tiles[i]);
                    tileReprs.add(tileRepr);
                }
            }
        });
    }

    public void correctTile() {
        if (foundTiles == tileReprs.size) {
            return;
        }
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                TileRepr tileRepr = tileReprs.get(foundTiles);
                SimpleSprite tileSprite = makeSprite(tileRepr.getTile());
                tileSprite.setPosition(calculateSpriteX(tileSprite, tileReprs.size, foundTiles), calculateSpriteY(tileSprite));
                tileRepr.setSprite(tileSprite);
                foundTiles++;
            }
        });
    }

    /**
     * Caluate the position of a tile given the tile number and the position.
     *
     * @param sprite
     * @param position the position of the sprite on the sizeConveyor. It starts at 0.
     * @return the x coordinate.
     */
    private float calculateSpriteX(SimpleSprite sprite, int noTile, int position) {
        float slotSpace = width / noTile;
        float x = position * slotSpace;
        x += slotSpace / 2;
        x -= sprite.getWidth() / 2;
        return x;
    }
}

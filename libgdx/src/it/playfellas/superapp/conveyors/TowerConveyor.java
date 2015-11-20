package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import it.playfellas.superapp.CompositeBgSprite;
import it.playfellas.superapp.SimpleSprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;

public class TowerConveyor extends Conveyor {

    private static final float slotStackXMult = 2f / 4f;

    private Array<TileRepr> completeStackReprs;
    private Array<TileRepr> slotStackReprs;
    private boolean clickable;

    public TowerConveyor(BaseListener listener) {
        super(listener);
        completeStackReprs = new Array<TileRepr>();
        slotStackReprs = new Array<TileRepr>();
        this.clickable = false;
    }

    @Override
    public Array<TileRepr> getTileReprs() {
        tileReprs.clear();
        tileReprs.addAll(completeStackReprs);
        tileReprs.addAll(slotStackReprs);
        return super.getTileReprs();
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
                for (int i = 0; i < 2; i++) {
                    SimpleSprite s = new SimpleSprite(bgTexture);
                    s.setSize(height, height);
                    s.setPosition(calculateSpriteX(s, i == 0), relativeY);
                    compositeBgSprite.addSprite(s);
                }
                setBgSprite(compositeBgSprite);
            }
        });
    }

    public void updateCompleteStack(final Tile[] stack) {
        completeStackReprs.clear();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < stack.length; i++) {
                    SimpleSprite tileSprite = makeSprite(stack[i]);
                    // Setting correct dimension
                    float tileSize = tileSprite.getWidth() * (1 - 0.25f * i);
                    tileSprite.setSize(tileSize, tileSize);
                    tileSprite = positionSprite(tileSprite, true);
                    TileRepr tileRepr = new TileRepr(tileSprite, stack[i]);
                    completeStackReprs.add(tileRepr);
                }
            }
        });
    }

    public void updateSlotStack(final Tile[] stack) {
        slotStackReprs.clear();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < stack.length; i++) {
                    if (stack[i] != null) {
                        SimpleSprite tileSprite = makeSprite(stack[i]);
                        // Setting correct dimension
                        float tileSize = tileSprite.getWidth() * (1 - 0.25f * i);
                        tileSprite.setSize(tileSize, tileSize);
                        tileSprite = positionSprite(tileSprite, false);
                        TileRepr tileRepr = new TileRepr(tileSprite, stack[i]);
                        slotStackReprs.add(tileRepr);
                    }
                }
            }
        });
    }

    /**
     * Handles a touche event
     */
    @Override
    public void touch(Vector3 touchPos) {
        if (!clickable) {
            return;
        }

        if (slotStackReprs.size == 0) {
            // the stack is empty, do not waste
            // your turn
            return;
        }

        float bigTileSize = height * tileHeightMult;
        float x = width * slotStackXMult;
        float y = relativeY + (height - bigTileSize) / 2;
        Rectangle stackRectangle = new Rectangle(x, y, bigTileSize, bigTileSize);
        if (stackRectangle.contains(touchPos.x, touchPos.y)) {
            listener.onTileClicked(null);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void start() {
        this.clickable = true;
    }

    @Override
    public void stop() {
        this.clickable = false;
    }

    @Override
    public void clear() {
        slotStackReprs.clear();
        completeStackReprs.clear();
    }

    @Override
    public void addTile(Tile tile) {
    }

    @Override
    public void addTile(TutorialTile tile) {
    }


    private SimpleSprite positionSprite(SimpleSprite sprite, boolean complete) {
        sprite.setPosition(calculateSpriteX(sprite, complete), calculateSpriteY(sprite));
        return sprite;
    }

    private float calculateSpriteX(SimpleSprite sprite, boolean complete) {
        float slotSpace = width / 4;
        float x = (complete ? 1 : 2) * slotSpace;
        x -= sprite.getWidth() / 2;
        x += height / 2;
        return x;
    }

}

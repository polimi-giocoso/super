package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import it.playfellas.superapp.CompositeBgSprite;
import it.playfellas.superapp.SimpleSprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.TutorialSprite;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;

public class MovingConveyor extends Conveyor {

    public static final int RIGHT = 1;
    public static final int LEFT = -1;

    private float rtt = 5;
    private int direction;
    private int pixelSpeed;
    private boolean running = false;

    private float bgFragmentWidth = 1f;
    private Texture bgFragmentTexture;
    private Texture tileRightTexture;
    private Texture tileWrongTexture;
    private CompositeBgSprite bgCompositeSprite;

    public MovingConveyor(BaseListener listener, float rtt, int direction) {
        super(listener);
        this.rtt = rtt;
        this.direction = direction;
        changeRTT(rtt);
    }

    @Override
    public void init(float h, float w, final float relativeY) {
        super.init(h, w, relativeY);
        updatePixelSpeed();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // Loading tutorial textures
                tileRightTexture = new Texture("_tutorial_right.png");
                tileWrongTexture = new Texture("_tutorial_wrong.png");
                // Preparing the background
                bgFragmentTexture = new Texture("_conveyor_bg_fragment.png");
                bgFragmentTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
                bgFragmentWidth = bgFragmentTexture.getWidth() / 1f;
                bgCompositeSprite = new CompositeBgSprite();
                float noFloatFragment = width / bgFragmentWidth;
                // Rounding
                int noFragment = (int) (noFloatFragment) + ((noFloatFragment % 1) == 0 ? 0 : 1);
                for (int i = -1; i < noFragment + 1; i++) {
                    SimpleSprite fragmentSprite = new SimpleSprite(bgFragmentTexture);
                    fragmentSprite.setBounds(i * bgFragmentWidth, relativeY, bgFragmentWidth, height);
                    bgCompositeSprite.addSprite(fragmentSprite);
                }
                setBgSprite(bgCompositeSprite);
            }
        });
    }

    @Override
    public void update() {
        // Moving tiles
        if (running) {
            lighten();
            // Update background
            updateBackground();
        } else {
            darken();
        }
        // Update tiles
        updateTiles();
    }

    private void updateTiles() {
        Iterator<TileRepr> it = new Array.ArrayIterator<TileRepr>(tileReprs, true); //to allow remove pass true
        while (it.hasNext()) {
            TileRepr tileRepr = it.next();
            SimpleSprite tileSprite = tileRepr.getSprite();
            if (tileSprite.isLeaving()) {
                tileSprite.decreaseSize();
            }
            if (direction == LEFT) {
                tileSprite.incrementX(-pixelSpeed * Gdx.graphics.getDeltaTime());
            } else {
                tileSprite.incrementX(pixelSpeed * Gdx.graphics.getDeltaTime());
            }
            if (tileSprite.getX() > width || tileSprite.getX() < -tileSprite.getWidth() || tileSprite.isDead()) {
                it.remove();
            }
        }
    }

    private void updateBackground() {
        Array<SimpleSprite> bgSprites = bgCompositeSprite.getSprites();
        boolean shift = false;
        if (direction == LEFT) {
            if (bgSprites.get(bgSprites.size - 1).getX() < width) {
                shift = true;
            }
        } else {
            if (bgSprites.get(0).getX() > 0) {
                shift = true;
            }
        }
        for (SimpleSprite sprite : bgSprites) {
            if (direction == LEFT) {
                if (shift) sprite.setX(sprite.getX() + bgFragmentWidth);
                sprite.incrementX(-pixelSpeed * Gdx.graphics.getDeltaTime());
            } else {
                if (shift) sprite.setX(sprite.getX() - bgFragmentWidth);
                sprite.incrementX(pixelSpeed * Gdx.graphics.getDeltaTime());
            }
        }
    }

    private void lighten() {
        setAlpha(1f);
    }

    private void darken() {
        setAlpha(0f);
        // TODO add X image
        // new TileRepr(new SimpleSprite(new Texture("x")), null);
    }

    private void setAlpha(float alpha) {
        Array<SimpleSprite> bgSprites = bgCompositeSprite.getSprites();
        for (SimpleSprite s : bgSprites) {
            s.setAlpha(alpha);
        }
    }

    /* API */

    /**
     * Starts moving all the tiles on the conveyor.
     */
    @Override
    public void start() {
        running = true;
    }

    /**
     * Stops moving all the tiles on the conveyor.
     */
    @Override
    public void stop() {
        running = false;
    }

    /**
     * Handles a touche event
     */
    @Override
    public void touch(Vector3 touchPos) {
        Iterator<TileRepr> it = new Array.ArrayIterator<TileRepr>(tileReprs);
        while (it.hasNext()) {
            TileRepr tileRepr = it.next();
            Rectangle tileRect = tileRepr.getSprite().getBoundingRectangle();

            // enlarge rectangle if the repr is too small
            float wh = height * tileHeightMult;
            float oldWidth = tileRect.getWidth();
            float oldX = tileRect.getX();
            float oldY = tileRect.getY();
            if (oldWidth < wh) {
                tileRect = new Rectangle(
                        oldX - ((wh - oldWidth) / 2f),
                        oldY - ((wh - oldWidth) / 2f),
                        wh, wh // a square
                );
            }

            if (tileRect.contains(touchPos.x, touchPos.y) && !tileRepr.getSprite().isLeaving()) {
                listener.onTileClicked(tileRepr.getTile());
                tileRepr.getSprite().setLeaving(true);
            }
        }
    }

    /**
     * @return a boolean that indicates if the conveyor is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Change the rtt (Round Trip Time) that is the time a tile spend to go through the conveyor.
     *
     * @param rtt the new round trip time.
     */
    public void changeRTT(float rtt) {
        this.rtt = rtt;
        updatePixelSpeed();
    }


    /**
     * Change the direction of movement of the conveyor.
     *
     * @param direction int representing the direction. (LEFT or RIGHT)
     */
    public void changeDirection(int direction) {
        if (direction != LEFT && direction != RIGHT) {
            throw new IllegalArgumentException(
                    "direction must be 1 or -1. See Conveyor.LEFT and Conveyor.RIGHT");
        }
        this.direction = direction;
    }

    /**
     * Adds a new tile to the conveyor.
     *
     * @param tile the tile to be drawn
     */
    public void addTile(final Tile tile) {
        if (!running) {
            return;
        }

        // Adding the new tile on the libgdx thread. Otherwise the libgdx context wouldn't be available.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                SimpleSprite tileSprite = makeSprite(tile);
                tileSprite.setPosition(calculateSpriteX(tileSprite), calculateSpriteY(tileSprite));
                TileRepr tileRepr = new TileRepr(tileSprite, tile);
                tileReprs.add(tileRepr);
            }
        });
    }

    /**
     * Adds a new TutorialTile to the conveyor.
     *
     * @param tutorialTile the TutorialTile to be drawn
     */
    @Override
    public void addTile(final TutorialTile tutorialTile) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                SimpleSprite tileSprite = makeSprite(tutorialTile.getTile());
                SimpleSprite tileBgSprite;
                if (tutorialTile.isRw()) {
                    tileBgSprite = new SimpleSprite(tileRightTexture);
                } else {
                    tileBgSprite = new SimpleSprite(tileWrongTexture);
                }
                tileBgSprite.setSize(height, height);
                TutorialSprite tutorialSprite = new TutorialSprite(tileBgSprite, tileSprite);
                tutorialSprite.setPosition(calculateSpriteX(tutorialSprite), calculateSpriteY(tutorialSprite));
                tutorialSprite.setPulse(tutorialTile.isRw());
                TileRepr tileRepr = new TileRepr(tutorialSprite, tutorialTile.getTile());
                tileReprs.add(tileRepr);
            }
        });
    }

    @Override
    public void clear() {
        for (TileRepr tileRepr : tileReprs) {
            tileRepr.getSprite().setLeaving(true);
        }
    }

    private void updatePixelSpeed() {
        pixelSpeed = (int) (width / rtt);
    }

    private float calculateSpriteX(SimpleSprite sprite) {
        float tileSize = sprite.getWidth();
        float x;
        if (direction == LEFT) {
            x = (int) width;
        } else {
            x = 0 - tileSize;
        }
        return x;
    }
}

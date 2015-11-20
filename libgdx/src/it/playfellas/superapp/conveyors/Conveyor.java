package it.playfellas.superapp.conveyors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import it.playfellas.superapp.CompositeBgSprite;
import it.playfellas.superapp.SimpleSprite;
import it.playfellas.superapp.TileRepr;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileType;
import it.playfellas.superapp.tiles.TutorialTile;

/**
 * Abstract class that must be extended to implement the different types of conveyor. It defines
 * the
 * abstract method to retrieve the sprites to be drawn by the Scene.
 */
public abstract class Conveyor {

    /**
     * Represent the percentage of the conveyor height occupied by a tile
     */
    protected static final float tileHeightMult = 0.8f;

    private CompositeBgSprite bgSprite;

    protected float width;
    protected float height;
    protected float relativeY;

    private boolean greyscale = false;

    protected BaseListener listener;

    protected Array<TileRepr> tileReprs;

    public Conveyor(BaseListener listener) {
        this.listener = listener;
        tileReprs = new Array<TileRepr>();
    }


    /**
     * Calculates the vertical position of a Tile to place it vertically centered within the conveyor.
     * It is the same for all the conveyors
     */
    protected int calculateSpriteY(SimpleSprite sprite) {
        int y = (int) (((height - sprite.getHeight()) / 2) + relativeY);
        return y;
    }

    /**
     * Constructs a new Sprite starting from a Tile. It applies all the needed transformations.
     * Remember to set bounds to the sprite in order to display it in the right position.
     * <p/>
     * IMPORTANT: call this method from the libgdx thread:
     * <p/>
     * Gdx.app.postRunnable()
     *
     * @param tile to be represented in a Sprite.
     * @return tileSprite
     */
    protected SimpleSprite makeSprite(Tile tile) {
        // Creating a sprite from a texture
        Texture tileTexture;
        // If in greyscale mode, load the greyscale version of the texture
        if (greyscale && tile.getType().equals(TileType.CONCRETE)) {
            tileTexture = new Texture(tile.getName().split("_")[0] + "_grayscale.png");
        } else {
            tileTexture = new Texture(tile.getName() + ".png");
        }
        tileTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        SimpleSprite tileSprite = new SimpleSprite(tileTexture);

        // Now apply all the needed transformations

        // Size
        float tileSize = ((height * tileHeightMult) * tile.getSize().getMultiplier());
        // Color
        if (!greyscale && tile.getType().equals(TileType.ABSTRACT)) {
            tileSprite.setColor(Color.valueOf(tile.getColor().hex().replace("#", "")));
        }
        // Direction
        if (tile.isDirectable()) {
            switch (tile.getDirection()) {
                case UP:
                    tileSprite.rotate90(true);
                    break;
                case RIGHT:
                    tileSprite.flip(true, false);
                    break;
                case DOWN:
                    tileSprite.rotate90(true);
                    tileSprite.flip(false, true);
                    break;
                case LEFT:
                    // Texture already directed correctly
                    break;
            }
        }
        tileSprite.setSize(tileSize, tileSize);
        return tileSprite;
    }

    /* PUBLIC METHODS */

    /**
     * Method to be called when the conveyor is added to a scene
     */
    public void init(float h, float w, float relativeY) {
        this.height = h;
        this.width = w;
        this.relativeY = relativeY;
    }

    /**
     * Method called every frame to allow the conveyor to update its state.
     */
    public abstract void update();

    public abstract void start();

    public abstract void stop();

    public abstract void addTile(Tile tile);

    public abstract void addTile(TutorialTile tile);

    public abstract void touch(Vector3 touchPos);

    public void clear() {
        tileReprs.clear();
    }

    public Array<TileRepr> getTileReprs() {
        return tileReprs;
    }

    public void setGreyscale(boolean greyscale) {
        this.greyscale = greyscale;
    }

    public CompositeBgSprite getBgSprite() {
        return bgSprite;
    }

    public void setBgSprite(CompositeBgSprite bgSprite) {
        this.bgSprite = bgSprite;
    }
}

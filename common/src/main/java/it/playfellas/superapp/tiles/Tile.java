package it.playfellas.superapp.tiles;

import it.playfellas.superapp.RandomUtils;

import java.io.Serializable;
import java.util.Arrays;

import it.playfellas.superapp.InternalConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by affo on 27/07/15.
 */
public class Tile implements Cloneable, Serializable {
    private static final String TAG = Tile.class.getSimpleName();
    @Getter
    private String name;
    @Getter
    private TileColor color;
    @Getter
    private boolean directable;
    @Getter
    private TileDirection direction; // not in db
    @Getter
    private TileShape shape;
    @Getter
    private TileType type;
    @Getter
    @Setter
    private TileSize size; // not in db

    public Tile(String name, TileColor color, boolean directable, TileShape shape, TileType type) {
        super();
        this.name = name;
        this.color = color;
        this.directable = directable;
        this.shape = shape;
        this.type = type;
        this.size = TileSize.XL;

        if (this.directable) {
            // by contract, directable tiles point to the left
            this.direction = InternalConfig.DEFAULT_DIRECTION;
        } else {
            this.direction = TileDirection.NONE;
        }
    }

    @Override
    public Tile clone() {
        return new Tile(name, color, directable, shape, type);
    }

    public Tile setDirection(TileDirection dir) {
        // TODO launch exceptions instead of logging
        if (!directable) {
            //Log.e(TAG, "Cannot set direction to undirectable tile: " + this.toString());
            return this;
        }

        if (dir == TileDirection.NONE) {
            //Log.e(TAG, "Cannot set direction to NONE");
            return this;
        }

        if (type == TileType.CONCRETE && (dir == TileDirection.UP || dir == TileDirection.DOWN)) {
            //Log.e(TAG, "Cannot set direction to concrete tile: " + dir);
            return this;
        }

        Tile t = this.clone();
        t.direction = dir;
        return t;
    }

    /**
     * Changes tile's direction basing on its type:
     * If the tile is CONCRETE, it swaps its direction (LEFT/RIGHT).
     * Else, it chooses a new random direction -- among all possible
     * ones (except from NONE) -- to be applied.
     * <p/>
     * No effect on undirectable tiles.
     *
     * @return a new tile that equals to this except from its direction
     */
    public Tile changeDirection() {
        if (type == TileType.CONCRETE) {
            return swapDirection();
        }

        TileDirection[] dirs = TileDirection.values();
        dirs = ArrayUtils.remove(dirs, ArrayUtils.indexOf(dirs, TileDirection.NONE));
        dirs = ArrayUtils.remove(dirs, ArrayUtils.indexOf(dirs, direction));
        return setDirection(RandomUtils.choice(Arrays.asList(dirs)));
    }

    /**
     * Swaps tile's direction:
     * UP <-> DOWN
     * LEFT <-> RIGHT
     * <p/>
     * No effect on undirectable tiles.
     *
     * @return a new tile that equals to this except from its direction
     */
    public Tile swapDirection() {
        switch (direction) {
            case UP:
                return setDirection(TileDirection.DOWN);
            case DOWN:
                return setDirection(TileDirection.UP);
            case LEFT:
                return setDirection(TileDirection.RIGHT);
            case RIGHT:
                return setDirection(TileDirection.LEFT);
            default:
                return this;
        }
    }

    @Override
    public String toString() {
        return "Tile{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", direction=" + direction +
                ", shape=" + shape +
                ", type=" + type +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Tile)) {
            return false;
        }

        Tile other = (Tile) o;
        return this.name.equals(other.name) &&
                this.color.equals(other.color) &&
                this.directable == other.directable &&
                this.direction.equals(other.direction) &&
                this.shape.equals(other.shape) &&
                this.type.equals(other.type) &&
                this.size.equals(other.size);
    }
}

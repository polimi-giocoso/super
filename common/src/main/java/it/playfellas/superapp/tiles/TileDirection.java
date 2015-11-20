package it.playfellas.superapp.tiles;

/**
 * Created by affo on 31/07/15.
 */
public enum TileDirection {
    UP,
    RIGHT,
    DOWN,
    LEFT,
    NONE;

    @Override
    public String toString() {
        return this.name();
    }
}

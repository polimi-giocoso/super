package it.playfellas.superapp.tiles;

/**
 * Created by affo on 03/08/15.
 */
public enum TileType {
    ABSTRACT,
    CONCRETE;

    @Override
    public String toString() {
        return this.name();
    }
}

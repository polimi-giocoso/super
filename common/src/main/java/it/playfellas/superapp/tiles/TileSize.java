package it.playfellas.superapp.tiles;

import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public enum TileSize {
    S((float) 0.25),
    M((float) 0.5),
    L((float) 0.75),
    XL(1);

    @Getter
    private float multiplier;

    TileSize(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public String toString() {
        return this.name();
    }
}

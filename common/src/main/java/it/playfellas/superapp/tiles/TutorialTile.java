package it.playfellas.superapp.tiles;

import lombok.Getter;

/**
 * Created by affo on 14/09/15.
 */
public class TutorialTile {
    @Getter
    private Tile tile;
    @Getter
    private boolean rw;

    public TutorialTile(Tile t, boolean rw) {
        this.tile = t;
        this.rw = rw;
    }

    @Override
    public String toString() {
        String srw = rw ? "RIGHT" : "WRONG";
        return srw + " -> " + tile.toString();
    }
}

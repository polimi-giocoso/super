package it.playfellas.superapp.events.ui;

import it.playfellas.superapp.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class UIRWEvent extends UIEvent {
    @Getter
    private Tile tile;
    @Getter
    private boolean right;

    public UIRWEvent(Tile t, boolean right) {
        this.tile = t;
        this.right = right;
    }
}

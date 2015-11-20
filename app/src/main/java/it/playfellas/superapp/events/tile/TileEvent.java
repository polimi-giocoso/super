package it.playfellas.superapp.events.tile;

import it.playfellas.superapp.events.InternalEvent;
import it.playfellas.superapp.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public abstract class TileEvent extends InternalEvent {
    @Getter
    private Tile tile;

    public TileEvent(Tile t) {
        this.tile = t;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + tile.toString();
    }
}

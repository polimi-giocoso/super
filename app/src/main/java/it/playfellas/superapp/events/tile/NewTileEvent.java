package it.playfellas.superapp.events.tile;

import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 27/07/15.
 */
public class NewTileEvent extends TileEvent {
    public NewTileEvent(Tile t) {
        super(t);
    }
}

package it.playfellas.superapp.events.tile;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class BaseTilesEvent extends NetEvent {
    @Getter
    Tile[] tiles;

    public BaseTilesEvent(Tile[] tiles) {
        this.tiles = tiles;
    }
}

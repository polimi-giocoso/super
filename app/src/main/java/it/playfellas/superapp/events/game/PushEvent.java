package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 03/09/15.
 */
public class PushEvent extends NetEvent {
    @Getter
    private Tile tile;

    public PushEvent(Tile tile) {
        this.tile = tile;
    }
}

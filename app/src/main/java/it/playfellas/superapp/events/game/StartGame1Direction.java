package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.tiles.TileDirection;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class StartGame1Direction extends StartGame1Event {
    @Getter
    private TileDirection baseDirection;

    public StartGame1Direction(Config1 conf, TileDirection baseDirection) {
        super(conf);
        this.baseDirection = baseDirection;
    }
}

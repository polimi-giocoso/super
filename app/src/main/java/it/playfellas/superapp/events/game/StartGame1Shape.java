package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.tiles.TileShape;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class StartGame1Shape extends StartGame1Event {
    @Getter
    private TileShape baseShape;

    public StartGame1Shape(Config1 conf, TileShape baseShape) {
        super(conf);
        this.baseShape = baseShape;
    }
}

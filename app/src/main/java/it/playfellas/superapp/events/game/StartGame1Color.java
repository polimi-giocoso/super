package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.tiles.TileColor;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class StartGame1Color extends StartGame1Event {
    @Getter
    private TileColor baseColor;

    public StartGame1Color(Config1 conf, TileColor baseColor) {
        super(conf);
        this.baseColor = baseColor;
    }
}

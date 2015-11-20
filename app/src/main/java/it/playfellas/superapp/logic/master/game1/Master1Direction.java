package it.playfellas.superapp.logic.master.game1;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.tiles.TileDirection;

/**
 * Created by affo on 07/08/15.
 */
public class Master1Direction extends Master1Controller {
    public static final TileDirection baseDir = TileDirection.RIGHT;
    private Config1 conf;

    public Master1Direction(Config1 conf) {
        super(conf);
        this.conf = conf;
    }

    @Override
    protected void onBeginStage() {

    }

    @Override
    protected void onEndStage() {

    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame1Direction(conf, baseDir);
    }
}

package it.playfellas.superapp.logic.master.game1;

import org.apache.commons.lang3.ArrayUtils;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.RandomUtils;
import it.playfellas.superapp.tiles.TileShape;

/**
 * Created by affo on 07/08/15.
 */
public class Master1Shape extends Master1Controller {
    private Config1 conf;
    private TileShape baseShape;

    public Master1Shape(Config1 conf) {
        super(conf);
        this.conf = conf;
        TileShape[] shapes = TileShape.values();
        // base should not be NONE...
        int noneIndex = ArrayUtils.indexOf(shapes, TileShape.NONE);
        shapes = ArrayUtils.remove(shapes, noneIndex);
        this.baseShape = RandomUtils.choice(shapes);
    }

    @Override
    protected void onBeginStage() {

    }

    @Override
    protected void onEndStage() {

    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame1Shape(conf, baseShape);
    }
}

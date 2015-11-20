package it.playfellas.superapp.logic.master.game23;

import java.util.Random;

import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.TileSize;

/**
 * Created by affo on 07/08/15.
 */
public class Master2Random extends Master2Controller {
    public Master2Random(TileSelector ts, Config2 conf) {
        super(ts, conf);
    }

    @Override
    protected TileSize[] getSizes() {
        return (new Random()).nextBoolean() ? getGrowing() : getDecreasing();
    }

    @Override
    protected void onEndStage() {

    }
}

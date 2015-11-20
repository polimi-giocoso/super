package it.playfellas.superapp.logic.master.game23;

import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.TileSize;

/**
 * Created by affo on 07/08/15.
 */
public class Master2Growing extends Master2Controller {
    public Master2Growing(TileSelector ts, Config2 conf) {
        super(ts, conf);
    }

    @Override
    protected TileSize[] getSizes() {
        return getGrowing();
    }

    @Override
    protected void onEndStage() {
        // does nothing
    }
}

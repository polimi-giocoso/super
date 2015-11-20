package it.playfellas.superapp.logic.slave.game23;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 02/09/15.
 */
public abstract class Slave23Controller extends SlaveController {
    private static final String TAG = Slave23Controller.class.getSimpleName();
    @Getter
    private Tile[] baseTiles;
    private TileDispenserWBaseTiles dispenser;

    private Object busListener;

    public Slave23Controller() {
        super();
        this.busListener = new Object() {
            @Subscribe
            public void onBaseTiles(BaseTilesEvent e) {
                baseTiles = e.getTiles();
                dispenser.setBaseTiles(baseTiles);
            }
        };
        TenBus.get().register(busListener);
    }

    @Override
    public void init() {
        super.init();
        this.dispenser = getDispenser();
    }

    @Override
    public void destroy() {
        super.destroy();
        TenBus.get().unregister(busListener);
    }

    @Override
    protected void onEndStage(EndStageEvent e) {
    }

    @Override
    protected void onEndGame(EndGameEvent e) {
    }

    @Override
    protected abstract TileDispenserWBaseTiles getDispenser();
}

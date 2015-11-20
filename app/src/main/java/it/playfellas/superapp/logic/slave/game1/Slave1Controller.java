package it.playfellas.superapp.logic.slave.game1;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 03/08/15.
 */
public abstract class Slave1Controller extends SlaveController {
    private static final String TAG = SlaveController.class.getSimpleName();
    private boolean dispenserToggle;
    private TileDispenser normalDispenser;
    private TileDispenser specialDispenser;

    // Object to be registered on `TenBus`.
    // We need it to make extending classes inherit
    // `@Subscribe` methods.
    private Object busListener;

    public Slave1Controller() {
        super();
        busListener = new Object() {
            @Subscribe
            public void onGameChange(ToggleGameModeEvent e) {
                toggleDispenser();
            }
        };
        TenBus.get().register(busListener);
    }

    @Override
    public void init() {
        super.init();
        normalDispenser = getDispenser();
        specialDispenser = getSpecialDispenser();
        dispenserToggle = true;
        setDispenser(normalDispenser);
    }

    @Override
    public void destroy() {
        super.destroy();
        TenBus.get().unregister(busListener);
    }

    @Override
    protected void onBeginStage(BeginStageEvent e) {
        // if we are in inverted mode, reset to normal mode
        if (!dispenserToggle) {
            toggleDispenser();
        }
    }

    /**
     * @return a new `TileDispenser` for special game mode
     */
    protected abstract TileDispenser getSpecialDispenser();

    protected synchronized boolean isNormalMode() {
        return dispenserToggle;
    }

    private synchronized void toggleDispenser() {
        dispenserToggle = !dispenserToggle;
        if (dispenserToggle) {
            setDispenser(normalDispenser);
        } else {
            setDispenser(specialDispenser);
        }
    }

    @Override
    public boolean checkTile(Tile t) {
        return this.isTileRight(t);
    }
}

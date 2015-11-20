package it.playfellas.superapp.ui.slave.game2;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.tile.NewTutorialTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.ControllerFactory;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game23.Slave2Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;
import it.playfellas.superapp.ui.slave.DisposingService;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave2Presenter extends SlavePresenter {
    private static final String TAG = Slave2Presenter.class.getSimpleName();

    private SlaveGame2Fragment slaveGame2Fragment;
    private Config2 config;
    private Slave2Controller slave2;

    public Slave2Presenter(TileSelector db, SlaveGame2Fragment slaveGame2Fragment, Config2 config) {
        TenBus.get().register(this);
        this.slaveGame2Fragment = slaveGame2Fragment;
        this.config = config;
        slave2 = ControllerFactory.slave2(db);
    }

    private void addTileToConveyors(Tile tile) {
        slaveGame2Fragment.getConveyorDown().addTile(tile);
    }

    private void addTutorialTileToConveyors(TutorialTile tile) {
        slaveGame2Fragment.getConveyorDown().addTile(tile);
    }

    @Override
    protected void startConveyors() {
        this.slaveGame2Fragment.getConveyorDown().start();
    }

    @Override
    protected void clearConveyors() {
        this.slaveGame2Fragment.getConveyorUp().clear();
        this.slaveGame2Fragment.getConveyorDown().clear();
    }

    @Override
    protected void stopConveyors() {
        this.slaveGame2Fragment.getConveyorDown().stop();
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event.getTile());
    }

    @Override
    protected void newTileEvent(NewTutorialTileEvent event) {
        this.addTutorialTileToConveyors(event.getTile());
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame2Fragment;
    }

    @Override
    public void pause() {
        DisposingService.stop();
        this.stopConveyors();
        this.clearConveyors();
    }

    /**
     * Method to kill the presenter
     */
    @Override
    public void kill() {
        super.kill();
        //unregister tenbus here and also into the superclass
        TenBus.get().unregister(this);
        this.pause();
    }

    @Override
    public void start() {
        DisposingService.start(slave2, config);
        this.stopConveyors();
        this.clearConveyors();
        this.startConveyors();
    }

    @Override
    protected void beginStageEvent(BeginStageEvent event) {
        //received a BeginStageEvent.
        Log.d(TAG, "------->BeginStageEvent received by the Slave Presenter");
        this.start();
    }

    @Override
    protected void endStageEvent(EndStageEvent event) {
        //received an EndStageEvent.
        Log.d(TAG, "------->EndStageEvent received by the Slave Presenter");
        this.clearConveyors();
        this.pause();
    }

    @Override
    protected void endGameEvent(EndGameEvent event) {
        Log.d(TAG, "------->EndGameEvent received by the Slave Presenter");
        this.kill();
        slaveGame2Fragment.endGame(event);
    }

    @Override
    protected void rttUpdateEvent(RTTUpdateEvent event) {
        if (slaveGame2Fragment.getConveyorDown() != null) {
            (slaveGame2Fragment.getConveyorDown()).changeRTT(event.getRtt());
        }
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        Tile[] tiles = e.getTiles();
        slaveGame2Fragment.showBaseTiles(tiles);
    }

    @Subscribe
    public void onUiRWEvent(UIRWEvent e) {
        if (e.isRight()) {
            slaveGame2Fragment.getConveyorUp().correctTile();
        }
    }
}

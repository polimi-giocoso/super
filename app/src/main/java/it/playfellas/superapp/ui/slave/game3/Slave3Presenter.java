package it.playfellas.superapp.ui.slave.game3;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.YourTurnEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.tile.NewTutorialTileEvent;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.ControllerFactory;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game23.Slave3Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.ui.slave.DisposingService;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave3Presenter extends SlavePresenter {
    private static final String TAG = Slave3Presenter.class.getSimpleName();

    private SlaveGame3Fragment slaveGame3Fragment;
    private Config3 config;
    private Slave3Controller slave3;

    public Slave3Presenter(TileSelector db, SlaveGame3Fragment slaveGame3Fragment, Config3 config) {
        TenBus.get().register(this);

        this.slaveGame3Fragment = slaveGame3Fragment;
        this.config = config;
        this.slave3 = ControllerFactory.slave3(db);
    }

    private void addTileToConveyors(NewTileEvent event) {
        this.slaveGame3Fragment.getConveyorDown().addTile(event.getTile());
    }

    private void addTutorialTileToConveyors(NewTutorialTileEvent event) {
        this.slaveGame3Fragment.getConveyorDown().addTile(event.getTile());
    }

    @Override
    protected void startConveyors() {
        this.slaveGame3Fragment.getConveyorUp().start();
        this.slaveGame3Fragment.getConveyorDown().start();
    }

    @Override
    protected void clearConveyors() {
        this.slaveGame3Fragment.getConveyorDown().clear();
    }

    @Override
    protected void stopConveyors() {
        this.slaveGame3Fragment.getConveyorUp().stop();
        this.slaveGame3Fragment.getConveyorDown().stop();
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame3Fragment;
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
        DisposingService.start(slave3, config);
        this.startConveyors();
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event);
    }

    @Override
    protected void newTileEvent(NewTutorialTileEvent event) {
        this.addTutorialTileToConveyors(event);
    }

    @Override
    protected void beginStageEvent(BeginStageEvent event) {
        Log.d(TAG, "------->BeginStageEvent received by the Slave Presenter");
        //because all player should stops. Only one of them will get the turn.
        this.pause();
    }

    @Override
    protected void endStageEvent(EndStageEvent event) {
        Log.d(TAG, "------->EndStageEvent received by the Slave Presenter");
        this.pause();
        this.slaveGame3Fragment.getConveyorUp().clear();
    }

    @Override
    protected void endGameEvent(EndGameEvent event) {
        Log.d(TAG, "------->EndGameEvent received by the Slave Presenter");
        this.kill();
        slaveGame3Fragment.endGame(event);
    }

    @Override
    protected void rttUpdateEvent(RTTUpdateEvent event) {
        if (slaveGame3Fragment.getConveyorDown() != null) {
            slaveGame3Fragment.getConveyorDown().changeRTT(event.getRtt());
        }
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        Tile[] baseTiles = e.getTiles();
        slaveGame3Fragment.updateCompleteStack(baseTiles);
    }

    @Subscribe
    public void onYourTurnEvent(YourTurnEvent e) {
        Log.d(TAG, "------->YourTurnEvent received by the Slave Presenter");
        if (e.getPlayerAddress().equals(TenBus.get().myBTAddress())) {
            Log.d(TAG, "------->YourTurnEvent: Hey, this is your turn!");
            this.start();
        } else {
            Log.d(TAG, "------->YourTurnEvent: Not your turn!");
            this.pause();
        }
        slaveGame3Fragment.updateSlotsStack(e.getStack());
    }

    @Subscribe
    public void onClickTileEvent(ClickedTileEvent e) {
        //pausePresenter
        this.pause();
    }
}

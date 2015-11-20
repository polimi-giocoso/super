package it.playfellas.superapp.ui.slave.game1;

import android.os.Handler;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Random;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.tile.NewTutorialTileEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.ControllerFactory;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game1.Slave1Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileDirection;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TutorialTile;
import it.playfellas.superapp.ui.slave.DisposingService;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave1Presenter extends SlavePresenter {
    private static final String TAG = Slave1Presenter.class.getSimpleName();

    private SlaveGame1Fragment slaveGame1Fragment;
    private Config1 config;
    private TileSelector db;
    private Slave1Controller slave1;
    private boolean isInverted = false;

    public Slave1Presenter(TileSelector db, SlaveGame1Fragment slaveGame1Fragment, Config1 config) {
        TenBus.get().register(this);

        this.slaveGame1Fragment = slaveGame1Fragment;
        this.config = config;
        this.db = db;
    }

    public void initControllerColor(TileColor tileColor) {
        if (config.getRule() == 1) { //called colorAgain or "Sagome" or shape
            slave1 = ControllerFactory.slaveColorAgain(db, tileColor);
        } else {
            //in all other cases use rule 0!
            //rule 0: color (config.getRule() == 0)
            slave1 = ControllerFactory.slaveColor(db, tileColor);
        }
    }

    public void initControllerDirection(TileDirection tileDirection) {
        slave1 = ControllerFactory.slaveDirection(db, tileDirection);
    }

    public void initControllerShape(TileShape tileShape) {
        slave1 = ControllerFactory.slaveShape(db, tileShape);
    }

    private void addTileToConveyors(Tile tile) {
        Random r = new Random();
        if (r.nextBoolean()) {
            slaveGame1Fragment.getConveyorUp().addTile(tile);
        } else {
            slaveGame1Fragment.getConveyorDown().addTile(tile);
        }
    }

    private void addTutorialTileToConveyors(TutorialTile tile) {
        Random r = new Random();
        if (r.nextBoolean()) {
            slaveGame1Fragment.getConveyorUp().addTile(tile);
        } else {
            slaveGame1Fragment.getConveyorDown().addTile(tile);
        }
    }

    @Override
    protected void startConveyors() {
        this.slaveGame1Fragment.getConveyorUp().start();
        this.slaveGame1Fragment.getConveyorDown().start();
    }

    @Override
    protected void clearConveyors() {
        this.slaveGame1Fragment.getConveyorUp().clear();
        this.slaveGame1Fragment.getConveyorDown().clear();
    }

    @Override
    protected void stopConveyors() {
        this.slaveGame1Fragment.getConveyorUp().stop();
        this.slaveGame1Fragment.getConveyorDown().stop();
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
    protected void beginStageEvent(BeginStageEvent event) {
        //received a BeginStageEvent.
        Log.d(TAG, "------->BeginStageEvent received by the Slave Presenter");
        slaveGame1Fragment.setInvertedBackground(false);
        isInverted = false;
        this.start();
    }

    @Override
    protected void endStageEvent(EndStageEvent event) {
        //received an EndStageEvent.
        Log.d(TAG, "------->EndStageEvent received by the Slave Presenter");
        this.pause();
    }

    @Override
    protected void endGameEvent(EndGameEvent event) {
        Log.d(TAG, "------->EndGameEvent received by the Slave Presenter");
        this.kill();
        slaveGame1Fragment.endGame(event);
    }


    @Override
    protected void rttUpdateEvent(RTTUpdateEvent event) {
        if (slaveGame1Fragment.getConveyorUp() != null) {
            slaveGame1Fragment.getConveyorUp().changeRTT(event.getRtt());
        }
        if (slaveGame1Fragment.getConveyorDown() != null) {
            slaveGame1Fragment.getConveyorDown().changeRTT(event.getRtt());
        }
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame1Fragment;
    }

    @Override
    public void pause() {
        DisposingService.stop();
        this.stopConveyors();
        this.clearConveyors();
    }

    @Override
    public void start() {
        DisposingService.start(slave1, config);
        this.stopConveyors();
        this.clearConveyors();
        this.startConveyors();
    }

    /**
     * Method to kill the presenter
     */
    @Override
    public void kill() {
        super.kill();

        //unregister tenbus here and also into the superclass
        TenBus.get().unregister(this);

        //stop the tiledisposer and conveyors
        DisposingService.stop();
        this.stopConveyors();
        this.clearConveyors();
    }

    @Subscribe
    public void onToggleGameMode(ToggleGameModeEvent e) {
        isInverted = !isInverted;
        this.clearConveyors();
        DisposingService.stop();
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                DisposingService.start(slave1, config);
            }
        }, 1000);
        slaveGame1Fragment.setInvertedBackground(isInverted);
    }
}

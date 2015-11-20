package it.playfellas.superapp.logic.slave.game23;

import android.util.Log;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 07/08/15.
 */
public class Slave2Controller extends Slave23Controller {
    private static final String TAG = Slave2Controller.class.getSimpleName();
    private SizeDispenser dispenser;
    private int rightPtr;

    public Slave2Controller(TileSelector ts) {
        super();
        this.dispenser = new SizeDispenser(ts);
        this.rightPtr = 0;
        this.dispenser.nextRight(rightPtr);
    }

    @Override
    protected void onBeginStage(BeginStageEvent e) {
        this.rightPtr = 0;
    }

    @Override
    protected synchronized boolean isTileRight(Tile t) {
        if (rightPtr >= getBaseTiles().length) {
            Log.d(TAG, "The stage should have been already finished: " + rightPtr + " right answers > " + getBaseTiles().length);
            return false;
        }

        if (t.equals(getBaseTiles()[rightPtr])) {
            rightPtr++;
            dispenser.nextRight(rightPtr);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean checkTile(Tile t) {
        if (rightPtr >= getBaseTiles().length) {
            Log.d(TAG, "The stage should have been already finished");
            return false;
        }

        return t.equals(getBaseTiles()[rightPtr]);
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }
}

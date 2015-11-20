package it.playfellas.superapp.logic.slave.game23;

import android.util.Log;

import java.util.Random;
import java.util.concurrent.Semaphore;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.RandomUtils;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 07/08/15.
 */
public abstract class TileDispenserWBaseTiles extends TileDispenser {
    private static final String TAG = TileDispenserWBaseTiles.class.getSimpleName();
    private Tile[] baseTiles;
    private int nextRight;

    private Semaphore sem;

    public TileDispenserWBaseTiles() {
        super();
        this.sem = new Semaphore(0);
        this.nextRight = -1;
    }

    public void setBaseTiles(Tile[] baseTiles) {
        this.baseTiles = baseTiles;
        sem.release();
    }

    @Override
    public Tile next() {
        try {
            Log.d(TAG, "Waiting for baseTiles to be ready");
            sem.acquire();
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for baseTiles", e);
            return getDistractor(baseTiles);
        }

        Log.d(TAG, "baseTiles ready");

        Random r = new Random();
        Tile nextTile;
        if (r.nextFloat() < InternalConfig.GAME23_TGT_PROB) {
            // ok, let's give a base tile
            if (nextRight >= 0 &&
                    nextRight < baseTiles.length &&
                    r.nextFloat() < InternalConfig.GAME23_RIGHT_PROB) {
                // ok, let's give the right one
                nextTile = baseTiles[nextRight];
            } else {
                nextTile = RandomUtils.choice(baseTiles);
            }
        } else {
            nextTile = getDistractor(baseTiles);
        }

        sem.release();
        return nextTile;
    }

    /**
     * Use this method to give an hint to the
     * dispenser on the next right tile expected
     *
     * @param index
     */
    public void nextRight(int index) {
        this.nextRight = index;
    }

    protected abstract Tile getDistractor(Tile[] baseTiles);
}

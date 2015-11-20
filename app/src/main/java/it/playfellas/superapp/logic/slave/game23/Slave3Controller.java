package it.playfellas.superapp.logic.slave.game23;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.YourTurnEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.StackClickEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 02/09/15.
 */
public class Slave3Controller extends Slave23Controller {
    private static final String TAG = Slave3Controller.class.getSimpleName();
    private TowerDispenser dispenser;
    private Tile[] stack;

    public Slave3Controller(TileSelector ts) {
        super();
        this.dispenser = new TowerDispenser(ts);
        this.dispenser.nextRight(0);

        TenBus.get().register(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        TenBus.get().unregister(this);
    }

    @Override
    protected synchronized void onBeginStage(BeginStageEvent e) {
    }

    private int emptySlot() {
        int i = 0;
        for (; i < stack.length && stack[i] != null; i++) ;
        return i;
    }

    private boolean cmpStack() {
        int i = 0;
        do {
            Tile st = stack[i];

            if (st == null) {
                break;
            }

            if (!st.equals(getBaseTiles()[i])) {
                return false;
            }

            i++;
        } while (i < stack.length);
        return true;
    }

    @Override
    protected synchronized boolean isTileRight(Tile t) {
        int i = emptySlot();
        if (i >= stack.length) {
            Log.d(TAG, "Stack exceeded!");
            return false;
        }

        return cmpStack() && getBaseTiles()[i].equals(t);
    }

    @Override
    public boolean checkTile(Tile t) {
        return this.isTileRight(t);
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }

    @Subscribe
    public void onConveyorClicked(ClickedTileEvent e) {
        TenBus.get().post(EventFactory.push(e.getTile()));
    }

    @Subscribe
    public synchronized void onStackClicked(StackClickEvent e) {
        TenBus.get().post(EventFactory.pop(emptySlot() != 0 && cmpStack()));
    }

    @Subscribe
    public synchronized void onStackUpdate(YourTurnEvent e) {
        this.stack = e.getStack();
        // send hint to dispenser:
        // if the stack is ok, then the next right
        // tile is the first empty slot in the stack.
        // Else we need a pop, thus we can give no hint.
        int hint = cmpStack() ? emptySlot() : -1;
        dispenser.nextRight(hint);
    }
}

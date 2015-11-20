package it.playfellas.superapp.logic.slave;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 28/07/15.
 * This is the controller of a player.
 * The `getTile` method has to be called from the UI (or from a presenter),
 * and it uses a `TileDispenser` to provide a new `Tile` to be placed.
 * Once instantiated, its `init` method has to be called
 * in order to fully configurate it.
 */
public abstract class SlaveController {
    private static final String TAG = SlaveController.class.getSimpleName();
    private TileDispenser dispenser;

    // Object to be registered on `TenBus`.
    // We need it to make extending classes inherit
    // `@Subscribe` methods.
    private Object busListener;

    public SlaveController() {
        super();

        busListener = new Object() {
            @Subscribe
            public void onTileClicked(ClickedTileEvent e) {
                Tile t = e.getTile();
                boolean rw = isTileRight(t);
                String rwWord = rw ? "Correct" : "Incorrect";
                Log.d(TAG, rwWord + " answer given");
                TenBus.get().post(EventFactory.rw(rw));
                TenBus.get().post(EventFactory.uiRWEvent(t, rw));
            }

            @Subscribe
            public void beginStage(BeginStageEvent e) {
                onBeginStage(e);
            }

            @Subscribe
            public void endStage(EndStageEvent e) {
                onEndStage(e);
            }

            @Subscribe
            public void endGame(EndGameEvent e) {
                onEndGame(e);
            }
        };
        TenBus.get().register(busListener);
    }

    /**
     * This method has to be called after
     * `SlaveController`s instantiation.
     */
    // We need to ensure that abstract method
    // `getDispenser` is called after object instantiation.
    // In this way, subclasses can take extra params in
    // constructor and use them in `getDispenser`.
    public void init() {
        dispenser = getDispenser();
    }

    /**
     * This method destroys the `SlaveController`.
     * After a call to this method the object becomes
     * unusable (e.g. it does not respond to Otto events
     * anymore).
     * If you extend this class, please remember to override
     * this method, call super.destroy() and perform
     * your "destroy" tasks.
     */
    public void destroy() {
        TenBus.get().unregister(busListener);
    }

    /**
     * Hooks for game phases.
     */

    protected abstract void onBeginStage(BeginStageEvent e);

    protected abstract void onEndStage(EndStageEvent e);

    protected abstract void onEndGame(EndGameEvent e);

    /**
     * Override to implement the logic of the game.
     * This method will be called every time a `Tile` is clicked
     * by the user. The correctness of the `Tile` could be determined
     * by the history of the clicks. In this case, it is important
     * to store, using this method, every clicked `Tile`.
     *
     * @param t the clicked `Tile`
     * @return true if the answer is right, false otherwise
     */
    protected abstract boolean isTileRight(Tile t);

    /**
     * This methods exposes the answer logic of the controller.
     * It can be called as many time as we want (it is stateless)
     * to check correctness of an answer
     *
     * @param t the answer
     * @return its correctness in that precise moment of the game
     */
    public abstract boolean checkTile(Tile t);

    /**
     * @return a new `TileDispenser` for this controller
     */
    protected abstract TileDispenser getDispenser();

    protected void setDispenser(TileDispenser td) {
        this.dispenser = td;
    }

    public Tile getTile() {
        return dispenser.next();
    }
}

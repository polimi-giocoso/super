package it.playfellas.superapp.logic.master.game23;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.RandomUtils;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.PopEvent;
import it.playfellas.superapp.events.game.PushEvent;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Color;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Name;
import it.playfellas.superapp.logic.db.query.QueryUtils;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 02/09/15.
 */
public class Master3Controller extends Master23Controller {
    private static final String TAG = Master3Controller.class.getSimpleName();
    private Config3 conf;
    private TileSelector ts;
    private Tile[] stack;
    private int stackPtr;

    public Master3Controller(TileSelector ts, Config3 conf) {
        super(conf);
        this.conf = conf;
        this.ts = ts;

        this.conf.setMaxScore(InternalConfig.NO_FIXED_TILES);

        TenBus.get().register(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        TenBus.get().unregister(this);
    }

    @Override
    protected Tile[] newBaseTiles() {
        final int size = InternalConfig.NO_FIXED_TILES;

        TileColor[] colors = QueryUtils.baseColors;

        colors = RandomUtils.choice(colors, size).toArray(new TileColor[size]);
        Tile[] tiles = new Tile[size];
        List<Name> names = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            tiles[i] = ts.random(1, new Conjunction(
                    // We want shape to be NONE because of db reasons
                    // emerged in last specifications.
                    new Shape(BinaryOperator.EQUALS, TileShape.NONE),
                    new Color(BinaryOperator.EQUALS, colors[i]),
                    new Type(BinaryOperator.EQUALS, TileType.ABSTRACT),
                    new Conjunction(names.toArray(new Name[names.size()]))
            )).get(0);
            names.add(new Name(BinaryOperator.DIFFERENT, tiles[i].getName()));
        }

        return tiles;
    }

    @Override
    protected synchronized void onBeginStage() {
        super.onBeginStage();
        this.stack = new Tile[InternalConfig.NO_FIXED_TILES];
        Arrays.fill(this.stack, null);
        this.stackPtr = 0;
        nextTurn();
    }

    private void nextTurn() {
        // cloning the stack before posting it
        // because... why not?!?!
        // Seriously, I cannot understand why do
        // I have to do this
        Tile[] cloned = new Tile[stack.length];
        Arrays.fill(cloned, null);
        for (int i = 0; i < cloned.length; i++) {
            if (stack[i] != null) {
                cloned[i] = stack[i].clone();
            }
        }
        TenBus.get().post(EventFactory.yourTurn(nextPlayer(), cloned));
    }

    @Override
    protected void onEndStage() {
        // does nothing
    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame3(conf);
    }

    @Subscribe
    public synchronized void onPush(PushEvent e) {
        if (stackPtr >= stack.length) {
            Log.d(TAG, "Exceeding stack length!");
        } else {
            stack[stackPtr] = e.getTile();
            stackPtr++;
        }

        if (getScore() < conf.getMaxScore()) {
            nextTurn();
        }
    }

    @Subscribe
    public synchronized void onPop(PopEvent e) {
        if (e.isWrongMove()) {
            // in case someone removed a
            // right tile
            decrementScore();
            notifyScore();
        }

        stackPtr--;
        if (stackPtr < 0) {
            stackPtr = 0;
        }
        stack[stackPtr] = null;

        nextTurn();
    }
}

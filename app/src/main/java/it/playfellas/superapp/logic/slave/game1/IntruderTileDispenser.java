package it.playfellas.superapp.logic.slave.game1;

import java.util.List;
import java.util.Random;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.Query;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 31/07/15.
 */
public abstract class IntruderTileDispenser extends TileDispenser {
    private static final int noCritical = InternalConfig.NO_CRITICAL;
    private static final int noEasy = InternalConfig.NO_EASY;
    private static final int noTarget = InternalConfig.NO_TARGET;
    private static final int tgtProb = InternalConfig.PROB_TARGET;
    private static final int easyProb = InternalConfig.PROB_EASY;

    private List<Tile> tgt;
    private List<Tile> critical;
    private List<Tile> easy;
    private Random rng;

    private TileSelector ts;

    public IntruderTileDispenser(TileSelector ts) {
        super();
        this.ts = ts;
        this.rng = new Random();
    }

    /**
     * This method has to be called after
     * `IntruderTileDispenser`s instantiation.
     */
    // We need to ensure that abstract methods
    // are called after object instantiation.
    // In this way, subclasses can take extra params in
    // constructor and use them.
    public void init() {
        this.tgt = getTargets(noTarget);
        this.critical = getCritical(noCritical, tgt);
        this.easy = getEasy(noEasy, tgt);
    }

    /**
     * Always use in subclasses before returning new tiles.
     * This method checks if the given list given is empty, if so, it applies
     * query `q` to obtain a new list of maximum size `n`.
     *
     * @param n     desired length (max) of the output list.
     * @param tiles the list to be validated.
     * @param q     a __less restrictive__ query (ideally, a query that should always
     *              produce results).
     * @return The same list passed, if it is valid, else, a new list obtained applying `q`.
     */
    protected List<Tile> validate(int n, List<Tile> tiles, Query q) {
        if (tiles.size() > 0) {
            return tiles;
        }

        return ts.random(n, q);
    }

    private List<Tile> trim(int n, List<Tile> tiles) {
        return n < tiles.size() ? tiles.subList(0, n) : tiles;
    }

    protected List<Tile> getTargets(int n) {
        if (tgt == null) {
            tgt = newTargets(n);
        }

        return trim(n, tgt);
    }

    protected List<Tile> getCritical(int n, List<Tile> targets) {
        if (critical == null) {
            critical = newCritical(n, targets);
        }

        return trim(n, critical);
    }

    protected List<Tile> getEasy(int n, List<Tile> targets) {
        if (easy == null) {
            easy = newEasy(n, targets);
        }

        return trim(n, easy);
    }

    abstract List<Tile> newTargets(int n);

    abstract List<Tile> newCritical(int n, List<Tile> targets);

    abstract List<Tile> newEasy(int n, List<Tile> targets);

    @Override
    public Tile next() {
        int choice = (int) (rng.nextFloat() * 100);

        if (choice <= tgtProb) {
            return RandomUtils.choice(this.tgt);
        }

        if (choice <= tgtProb + easyProb) {
            return RandomUtils.choice(this.easy);
        }

        return RandomUtils.choice(this.critical);
    }
}

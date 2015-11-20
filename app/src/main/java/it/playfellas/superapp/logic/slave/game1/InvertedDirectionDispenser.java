package it.playfellas.superapp.logic.slave.game1;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 06/08/15.
 */
public class InvertedDirectionDispenser extends IntruderTileDispenser {
    private DirectionIntruderDispenser normal;

    public InvertedDirectionDispenser(TileSelector ts, DirectionIntruderDispenser normal) {
        super(ts);
        this.normal = normal;
    }

    @Override
    List<Tile> newTargets(int n) {
        List<Tile> newTgts = new ArrayList<>();
        for (Tile t : normal.getTargets(n)) {
            newTgts.add(t.swapDirection());
        }
        return newTgts;
    }

    @Override
    List<Tile> newCritical(int n, List<Tile> targets) {
        return normal.getTargets(n);
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        return normal.getEasy(n, targets);
    }
}

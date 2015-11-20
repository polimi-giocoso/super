package it.playfellas.superapp.logic.slave.game1;

import java.util.List;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 06/08/15.
 */
public class ShapeIntruderDispenser extends IntruderTileDispenser {
    private TileSelector ts;
    private TileShape base;

    public ShapeIntruderDispenser(TileSelector ts, TileShape baseShape) {
        super(ts);
        this.ts = ts;
        this.base = baseShape;
    }

    @Override
    List<Tile> newTargets(int n) {
        return ts.random(n, new Conjunction(new Shape(BinaryOperator.EQUALS, base), new Type(BinaryOperator.EQUALS, TileType.CONCRETE)));
    }

    @Override
    List<Tile> newCritical(int n, List<Tile> targets) {
        return ts.random(n, new Conjunction(new Shape(BinaryOperator.EQUALS, TileShape.NONE), new Type(BinaryOperator.EQUALS, TileType.CONCRETE)));
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        // there is not a specified distinction
        // between easy and critical.
        return newCritical(n, targets);
    }
}

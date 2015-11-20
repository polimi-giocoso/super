package it.playfellas.superapp.logic.slave.game1;

import java.util.List;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Color;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 06/08/15.
 */
public class ColorAgainIntruderDispenser extends IntruderTileDispenser {
    // In this case, toggleMode is only representation logic.
    // It is the UI that has to present the same images, but BORDER_ONLY.
    private TileSelector ts;
    private TileColor base;

    public ColorAgainIntruderDispenser(TileSelector ts, TileColor baseColor) {
        super(ts);
        this.ts = ts;
        this.base = baseColor;
    }

    @Override
    List<Tile> newTargets(int n) {
        return ts.random(n, new Conjunction(new Color(BinaryOperator.EQUALS, base), new Type(BinaryOperator.EQUALS, TileType.CONCRETE)));
    }

    @Override
    List<Tile> newCritical(int n, List<Tile> targets) {
        return newEasy(n, targets); // same query as easy
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        return ts.random(n, new Conjunction(new Color(BinaryOperator.DIFFERENT, base), new Type(BinaryOperator.EQUALS, TileType.CONCRETE)));
    }
}

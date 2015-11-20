package it.playfellas.superapp.logic.slave.game1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Color;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Disjunction;
import it.playfellas.superapp.logic.db.query.Query;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileShape;

/**
 * Created by affo on 31/07/15.
 */
public class ColorIntruderDispenser extends IntruderTileDispenser {
    private TileColor baseColor;
    private TileSelector ts;

    public ColorIntruderDispenser(TileSelector ts, TileColor baseColor) {
        super(ts);
        this.ts = ts;
        this.baseColor = baseColor;
    }

    @Override
    List<Tile> newTargets(int n) {
        return ts.random(n, new Color(BinaryOperator.EQUALS, baseColor));
    }

    @Override
    List<Tile> newCritical(int n, List<Tile> targets) {
        // COLOR | DIRECTION | SHAPE
        // diff  |   any     |  same
        Set<TileShape> shapes = new HashSet<>();
        for (Tile t : targets) {
            shapes.add(t.getShape());
        }

        Query[] params = new Query[shapes.size()];
        int i = 0;
        for (TileShape s : shapes) {
            params[i] = new Shape(BinaryOperator.EQUALS, s);
            i++;
        }

        Query simpleOne = new Color(BinaryOperator.DIFFERENT, baseColor);
        List<Tile> res = ts.random(n, new Conjunction(simpleOne, new Disjunction(params)));
        return validate(n, res, simpleOne);
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        // COLOR | DIRECTION | SHAPE
        // diff  |   any     |  diff
        Set<TileShape> shapes = new HashSet<>();
        for (Tile t : targets) {
            shapes.add(t.getShape());
        }

        Query[] params = new Query[shapes.size()];
        int i = 0;
        for (TileShape s : shapes) {
            params[i] = new Shape(BinaryOperator.DIFFERENT, s);
            i++;
        }

        Query simpleOne = new Color(BinaryOperator.DIFFERENT, baseColor);
        List<Tile> res = ts.random(n, new Conjunction(simpleOne, new Conjunction(params)));
        return validate(n, res, simpleOne);
    }
}

package it.playfellas.superapp.logic.slave.game23;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 01/09/15.
 */
public class SizeDispenser extends TileDispenserWBaseTiles {
    private TileSelector ts;

    public SizeDispenser(TileSelector ts) {
        super();
        this.ts = ts;
    }

    @Override
    protected Tile getDistractor(Tile[] baseTiles) {
        TileShape baseShape = baseTiles[0].getShape();
        return ts.random(1, new Conjunction(new Shape(BinaryOperator.DIFFERENT, baseShape), new Type(BinaryOperator.EQUALS, TileType.ABSTRACT))).get(0);
    }
}

package it.playfellas.superapp.logic.slave.game23;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Query;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 02/09/15.
 */
public class TowerDispenser extends TileDispenserWBaseTiles {
    private TileSelector ts;
    private Tile[] distractors;

    public TowerDispenser(TileSelector ts) {
        super();
        this.ts = ts;
    }

    @Override
    protected Tile getDistractor(Tile[] baseTiles) {
        if (distractors == null) {
            distractors = newDistractors(baseTiles);
        }
        return RandomUtils.choice(distractors);
    }

    private Tile[] newDistractors(Tile[] baseTiles) {
        // getting abstract tiles with different shapes.
        Query[] andQ = new Shape[baseTiles.length];
        for (int i = 0; i < baseTiles.length; i++) {
            andQ[i] = new Shape(BinaryOperator.DIFFERENT, baseTiles[i].getShape());
        }
        Query q = new Conjunction(new Conjunction(andQ), new Type(BinaryOperator.EQUALS, TileType.ABSTRACT));
        return ts.random(InternalConfig.GAME3_NO_DISTRACTORS, q).toArray(new Tile[InternalConfig.GAME3_NO_DISTRACTORS]);
    }
}

package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.tiles.TileShape;

/**
 * Created by affo on 03/08/15.
 */
public class Shape extends Atom {
    private TileShape shape;

    public Shape(BinaryOperator op, TileShape shape) {
        super(op);
        this.shape = shape;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_SHAPE;
    }

    @Override
    protected String getValue() {
        return quoteValue(this.shape.toString());
    }
}

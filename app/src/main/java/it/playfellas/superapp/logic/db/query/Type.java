package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 06/08/15.
 */
public class Type extends Atom {
    private TileType type;

    public Type(BinaryOperator op, TileType type) {
        super(op);
        this.type = type;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_TYPE;
    }

    @Override
    protected String getValue() {
        return quoteValue(this.type.toString());
    }
}

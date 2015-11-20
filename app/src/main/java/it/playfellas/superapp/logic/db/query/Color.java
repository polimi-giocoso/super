package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.tiles.TileColor;

/**
 * Created by affo on 03/08/15.
 */
public class Color extends Atom {
    private TileColor color;

    public Color(BinaryOperator op, TileColor color) {
        super(op);
        this.color = color;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_COLOR;
    }

    @Override
    protected String getValue() {
        return quoteValue(this.color.toString());
    }
}

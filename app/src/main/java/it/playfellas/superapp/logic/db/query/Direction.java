package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;

/**
 * Created by affo on 03/08/15.
 */
public class Direction extends Atom {
    private boolean directable;

    public Direction(BinaryOperator op, boolean directable) {
        super(op);
        this.directable = directable;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_DIRECTABLE;
    }

    @Override
    protected String getValue() {
        return directable ? Integer.toString(1) : Integer.toString(0);
    }
}

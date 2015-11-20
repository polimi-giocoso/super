package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;

/**
 * Created by affo on 17/09/15.
 */
public class Name extends Atom {
    private String name;

    public Name(BinaryOperator op, String name) {
        super(op);
        this.name = name;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_NAME;
    }

    @Override
    protected String getValue() {
        return quoteValue(this.name);
    }
}

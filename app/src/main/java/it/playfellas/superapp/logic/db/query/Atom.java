package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by affo on 06/08/15.
 */
public abstract class Atom extends Query {
    private BinaryOperator op;

    public Atom(BinaryOperator op) {
        this.op = op;
    }

    @Override
    public String get() {
        return StringUtils.join(new String[]{getColumnName(), op.toString(), getValue()}, " ");
    }

    protected abstract String getColumnName();

    protected String quoteValue(String value) {
        return "'" + value + "'";
    }

    protected abstract String getValue();
}

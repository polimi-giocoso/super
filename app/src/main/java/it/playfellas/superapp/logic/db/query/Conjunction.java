package it.playfellas.superapp.logic.db.query;

/**
 * Created by affo on 03/08/15.
 */
public class Conjunction extends Operation {
    public Conjunction(Query... params) {
        super(params);
    }

    public Conjunction(boolean not, Query... params) {
        super(not, params);
    }

    @Override
    protected BinaryOperator getOp() {
        return BinaryOperator.AND;
    }
}

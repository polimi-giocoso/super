package it.playfellas.superapp.logic.db.query;

/**
 * Created by affo on 03/08/15.
 */
public class Disjunction extends Operation {
    public Disjunction(Query... params) {
        super(params);
    }

    public Disjunction(boolean not, Query... params) {
        super(not, params);
    }

    @Override
    protected BinaryOperator getOp() {
        return BinaryOperator.OR;
    }
}

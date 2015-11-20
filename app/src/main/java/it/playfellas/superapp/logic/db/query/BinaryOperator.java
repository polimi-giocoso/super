package it.playfellas.superapp.logic.db.query;

/**
 * Created by affo on 06/08/15.
 */
public enum BinaryOperator {
    DIFFERENT(" <> "),
    EQUALS(" = "),
    AND(" AND "),
    OR(" OR ");

    private String op;

    BinaryOperator(String op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return op;
    }
}

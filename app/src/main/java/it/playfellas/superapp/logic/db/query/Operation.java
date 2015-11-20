package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by affo on 06/08/15.
 */
public abstract class Operation extends Query {
    private Query[] params;
    private boolean not;

    public Operation(Query... params) {
        this.not = false;
        this.params = params;
    }

    public Operation(boolean not, Query... params) {
        this.not = not;
        this.params = params;
    }

    private String getNot() {
        return not ? Query.NOT : "";
    }

    @Override
    public String get() {
        if (params.length == 0) {
            return "";
        }

        if (params.length == 1) {
            return getNot() + " " + params[0].get();
        }

        List<String> paramString = new ArrayList<>();
        for (int i = 0; i < params.length; i++) {
            String p = params[i].get();
            if (!p.isEmpty()) {
                paramString.add(p);
            }
        }
        String where = StringUtils.join(paramString.toArray(new String[paramString.size()]), getOp().toString());
        return getNot() + " ( " + where + " )";
    }

    protected abstract BinaryOperator getOp();
}

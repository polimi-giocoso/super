package it.playfellas.superapp.logic.db;

import java.util.List;

import it.playfellas.superapp.logic.db.query.Query;
import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 03/08/15.
 */
public interface TileSelector {
    /**
     * Returns `n` (at maximum) random Tiles matching the specs given.
     * A spec could be equal to `ANY` leaving the selector free in selection.
     * Tiles returned must be of type `ABSTRACT` or `CONCRETE` (NOT `PHOTO`).
     *
     * @param n the max `size()` of the value returned
     * @return a `List` of `n` tiles matching the given specs
     */
    List<Tile> random(int n, Query query);
}

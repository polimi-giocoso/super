package it.playfellas.superapp.logic.slave;

import java.util.Iterator;

import it.playfellas.superapp.tiles.Tile;

/**
 * Created by affo on 27/07/15.
 * This class is an automatic `Tile` dispenser.
 * It returns `Tile`s infinitely through its method `next`.
 * `hasNext` method always returns `true` and `remove` is not implemented.
 * The method `next` has to be implemented by extending classes.
 * Each `TileDispenser` class, in fact, has to implement its own logic
 * in dispensing `Tile`s.
 */
public abstract class TileDispenser implements Iterator<Tile> {
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

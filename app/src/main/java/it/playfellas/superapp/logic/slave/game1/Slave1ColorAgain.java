package it.playfellas.superapp.logic.slave.game1;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.tiles.TileColor;

/**
 * Created by affo on 06/08/15.
 */
public class Slave1ColorAgain extends Slave1Color {
    private ColorAgainIntruderDispenser dispenser;

    public Slave1ColorAgain(TileSelector ts, TileColor baseColor) {
        super(ts, baseColor);
        dispenser = new ColorAgainIntruderDispenser(ts, super.getBaseColor());
        dispenser.init();
    }

    @Override
    protected TileDispenser getDispenser() {
        return dispenser;
    }

    @Override
    protected TileDispenser getSpecialDispenser() {
        // In special mode, we always return same tiles.
        // It will be the UI that will represent tiles
        // in "BORDER_ONLY mode".
        return dispenser;
    }
}

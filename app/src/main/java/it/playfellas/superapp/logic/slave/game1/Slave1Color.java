package it.playfellas.superapp.logic.slave.game1;

import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import lombok.Getter;

/**
 * Created by affo on 03/08/15.
 */
public class Slave1Color extends Slave1Controller {
    private ColorIntruderDispenser normalDispenser;
    private IntruderTileDispenser specialDispenser;
    @Getter
    private TileColor baseColor;
    private TileSelector ts;

    public Slave1Color(TileSelector ts, TileColor baseColor) {
        super();
        this.ts = ts;
        this.baseColor = baseColor;
        this.normalDispenser = new ColorIntruderDispenser(ts, baseColor);
        this.specialDispenser = new IntruderDispenserInverter(ts, normalDispenser);
        normalDispenser.init();
        specialDispenser.init();
    }

    @Override
    protected void onEndStage(EndStageEvent e) {

    }

    @Override
    protected void onEndGame(EndGameEvent e) {

    }

    @Override
    protected boolean isTileRight(Tile t) {
        // right answer is when an intruder has been
        // selected. An intruder is such when it has
        // a color different from base one.
        boolean rw = !t.getColor().equals(baseColor);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    protected TileDispenser getDispenser() {
        return normalDispenser;
    }

    @Override
    protected TileDispenser getSpecialDispenser() {
        return specialDispenser;
    }
}

package it.playfellas.superapp.logic.slave.game1;

import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileShape;
import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class Slave1Shape extends Slave1Controller {
    private ShapeIntruderDispenser normal;
    private IntruderTileDispenser special;
    @Getter
    private TileShape baseShape;

    public Slave1Shape(TileSelector ts, TileShape baseShape) {
        super();
        this.baseShape = baseShape;
        this.normal = new ShapeIntruderDispenser(ts, baseShape);
        this.special = new IntruderDispenserInverter(ts, normal);
        normal.init();
        special.init();
    }

    @Override
    protected void onEndStage(EndStageEvent e) {

    }

    @Override
    protected void onEndGame(EndGameEvent e) {

    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean rw = !t.getShape().equals(baseShape);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    protected TileDispenser getSpecialDispenser() {
        return special;
    }

    @Override
    protected TileDispenser getDispenser() {
        return normal;
    }
}

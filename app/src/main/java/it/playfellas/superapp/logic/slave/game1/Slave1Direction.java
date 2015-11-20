package it.playfellas.superapp.logic.slave.game1;

import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileDirection;
import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class Slave1Direction extends Slave1Controller {
    private DirectionIntruderDispenser normal;
    private InvertedDirectionDispenser special;
    @Getter
    private TileDirection baseDirection;

    public Slave1Direction(TileSelector ts, TileDirection baseDirection) {
        super();
        this.baseDirection = baseDirection;
        this.normal = new DirectionIntruderDispenser(ts, baseDirection);
        this.special = new InvertedDirectionDispenser(ts, normal);
        this.normal.init();
        this.special.init();
    }

    @Override
    protected void onEndStage(EndStageEvent e) {

    }

    @Override
    protected void onEndGame(EndGameEvent e) {

    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean rw = !t.getDirection().equals(baseDirection);
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

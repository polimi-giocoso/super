package it.playfellas.superapp.logic.master.game23;

import org.apache.commons.lang3.ArrayUtils;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.RandomUtils;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Color;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TileSize;
import it.playfellas.superapp.tiles.TileType;

/**
 * Created by affo on 07/08/15.
 */
public abstract class Master2Controller extends Master23Controller {
    private Config2 conf;
    private TileSelector ts;

    public Master2Controller(TileSelector ts, Config2 conf) {
        super(conf);
        this.conf = conf;
        this.ts = ts;

        this.conf.setMaxScore(InternalConfig.NO_FIXED_TILES * TenBus.get().noDevices());
    }

    @Override
    protected Tile[] newBaseTiles() {
        // select a new random Tile basing on its shape
        TileShape[] shapes = TileShape.values();
        // baseShape should not be NONE...
        int noneIndex = ArrayUtils.indexOf(shapes, TileShape.NONE);
        shapes = ArrayUtils.remove(shapes, noneIndex);
        Tile base = ts.random(1, new Conjunction(
                new Shape(BinaryOperator.EQUALS, RandomUtils.choice(shapes)),
                new Type(BinaryOperator.EQUALS, TileType.ABSTRACT),
                new Color(BinaryOperator.DIFFERENT, TileColor.BLACK))).get(0);
        Tile[] baseTiles = new Tile[InternalConfig.NO_FIXED_TILES];
        for (int i = 0; i < InternalConfig.NO_FIXED_TILES; i++) {
            baseTiles[i] = base.clone();
        }
        TileSize[] sizes = getSizes();
        setSizes(baseTiles, sizes);
        return baseTiles;
    }

    private void setSizes(Tile[] tiles, TileSize[] sizes) {
        for (int i = 0; i < tiles.length; i++) {
            TileSize s = i < sizes.length ? sizes[i] : sizes[sizes.length - 1];
            tiles[i].setSize(s);
        }
    }

    /**
     * Use in extending classes, plz
     */

    protected TileSize[] getGrowing() {
        return TileSize.values(); // returned in the order they are declared
    }

    protected TileSize[] getDecreasing() {
        TileSize[] sizes = TileSize.values();
        ArrayUtils.reverse(sizes);
        return sizes;
    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame2(conf);
    }

    protected abstract TileSize[] getSizes();
}

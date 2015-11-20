package it.playfellas.superapp.events.tile;

import it.playfellas.superapp.events.InternalEvent;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;
import lombok.Getter;

/**
 * Created by affo on 14/09/15.
 */
public class NewTutorialTileEvent extends InternalEvent {
    @Getter
    private TutorialTile tile;

    public NewTutorialTileEvent(Tile t, boolean rw) {
        this.tile = new TutorialTile(t, rw);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + tile.toString();
    }
}

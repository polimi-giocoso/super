package it.playfellas.superapp.ui.slave;

import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

public class MovingConveyorListener extends BaseListener {
  @Override public void onTileClicked(Tile tile) {
    TenBus.get().post(new ClickedTileEvent(tile));
  }
}

package it.playfellas.superapp.ui.slave;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.listeners.BaseListener;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;

public class TowerConveyorListener extends BaseListener {
  @Override public void onTileClicked(Tile tile) {
    TenBus.get().post(EventFactory.stackClick());
  }
}

package it.playfellas.superapp.listeners;

import it.playfellas.superapp.tiles.Tile;

public abstract class BaseListener {
  public abstract void onTileClicked(Tile tile);
}

package it.playfellas.superapp;

import it.playfellas.superapp.tiles.Tile;

public class TileRepr {
    private SimpleSprite sprite;
    private Tile tile;

    public TileRepr(SimpleSprite sprite, Tile tile) {
        this.sprite = sprite;
        this.tile = tile;
    }

    public SimpleSprite getSprite() {
        return sprite;
    }

    public void setSprite(SimpleSprite sprite) {
        this.sprite = sprite;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}

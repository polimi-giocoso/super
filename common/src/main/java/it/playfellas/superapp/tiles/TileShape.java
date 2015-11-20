package it.playfellas.superapp.tiles;

/**
 * Created by affo on 31/07/15.
 */
public enum TileShape {
    NONE,
    CUBE,
    TRIANGLE,
    PARALLELOGRAM,
    RHOMBUS,
    PARALLELEPIPED,
    SQUARE,
    TRAPEZOID,
    STAR,
    CIRCLE,
    PENTAGON,
    RECTANGLE;

    @Override
    public String toString() {
        return this.name();
    }
}

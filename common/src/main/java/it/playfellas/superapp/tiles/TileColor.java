package it.playfellas.superapp.tiles;


/**
 * Created by affo on 31/07/15.
 */
public enum TileColor {
    NONE(42),
    WHITE(0xffffff),
    BLACK(0x424242),
    ORANGE(0xf39221),
    GREEN(0X7ebf76),
    LBLUE(0X55c1f0),
    YELLOW(0xfaea18),
    PINK(0xf9cabd),
    RED(0xea5351),
    VIOLET(0x7a3a8e),
    BROWN(0x6d4c42),
    BLUE(0x3781c3);

    private String hex;

    TileColor(int androidColor) {
        this.hex = String.format("#%06X", (0xFFFFFF & androidColor));
    }

    public String hex() {
        return hex;
    }

    @Override
    public String toString() {
        return this.name();
    }
}

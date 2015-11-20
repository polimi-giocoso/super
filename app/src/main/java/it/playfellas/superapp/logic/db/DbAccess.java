package it.playfellas.superapp.logic.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.db.query.Query;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TileType;


/**
 * Class to access to the db.
 */
public class DbAccess implements TileSelector {

    private DbAdapter dbHelper;

    /**
     * Class to access to the db.
     *
     * @param context The Activity context.
     */
    public DbAccess(Context context) {
        dbHelper = new DbAdapter(context);
    }

    @Override
    public List<Tile> random(int n, Query query) {
        List<Tile> tiles = new ArrayList<>();
        List<Integer> randomIndexes = new ArrayList<>();

        String whereClause = query.get();
        dbHelper.open();
        Cursor cursor = dbHelper.fetchByQuery(InternalConfig.TABLE_NAME, whereClause);
        for (int i = 0; i < cursor.getCount(); i++) {
            randomIndexes.add(i);
        }
        Collections.shuffle(randomIndexes);
        int maxLength = n > randomIndexes.size() ? randomIndexes.size() : n;
        randomIndexes = randomIndexes.subList(0, maxLength);

        for (Integer pos : randomIndexes) {
            cursor.moveToPosition(pos);
            tiles.add(getTile(cursor));
        }
        dbHelper.close();
        return tiles;
    }

    /**
     * Add a new {@link Tile} to the {@code tableName}.
     *
     * @param tableName String that represents the tablename.
     * @param tile      The object to add.
     * @throws DbException An exception that explains the reason of the problem.
     */
    public void add(String tableName, Tile tile) throws DbException {
        dbHelper.open();
        long ret = dbHelper.insertTupleObject(tableName, tile);
        dbHelper.close();

        //ir ret==-1, tile isn't added
        if (ret == -1) {
            throw new DbException(DbException.Reason.NOTADDED);
        }
    }

    /**
     * Method to gell all the element in the {@code tableName}.
     *
     * @param tableName String that represents the tablename.
     * @return A {@code List<Tile>} of the objects.
     */
    public List<Tile> getAll(String tableName) {
        List<Tile> tiles = new ArrayList<>();

        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllData(tableName);

        while (cursor.moveToNext()) {
            tiles.add(getTile(cursor));
        }

        dbHelper.close();
        return tiles;
    }

    /**
     * Method to log the {@code tableName}.
     *
     * @param tableName String that represents the tablename.
     */
    public void logDb(String tableName) {
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllData(tableName);
        while (cursor.moveToNext()) {
            Log.d("DB:" + InternalConfig.DATABASE_NAME + "_TABLE:" + tableName, getTile(cursor).toString());
        }
        dbHelper.close();
    }

    private Tile getTile(Cursor cursor) {
        String url = cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_NAME));
        TileColor color = TileColor.valueOf(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_COLOR)));
        TileShape shape = TileShape.valueOf(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_SHAPE)));
        int dir = cursor.getInt(cursor.getColumnIndex(InternalConfig.KEY_DIRECTABLE));
        TileType type = TileType.valueOf(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_TYPE)));
        return new Tile(url, color, dir != 0, shape, type);
    }
}

package it.playfellas.superapp.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.tiles.Tile;

/**
 * Class with queries.
 */
class DbAdapter {
    private Context context;
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DbAdapter(Context context) {
        this.context = context;
    }

    /**
     * Method to open db.
     *
     * @return A {@link DbAdapter}, i.e. the instance of this class.
     * @throws SQLException that indicates there was an error with SQL parsing or execution.
     */
    public DbAdapter open() throws SQLException {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Method to close db. Remember to close db after every query!!!!!!
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Method to add a new row in the db, specifying every field.
     *
     * @param tableName  The table name.
     * @param url        Tile's url.
     * @param color      Tile's color.
     * @param shape      Tile's shape.
     * @param directable If the tile is directable or not.
     * @param type       Tile's type.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertTupleValues(String tableName, String url, String color, String shape, boolean directable, String type) {
        ContentValues initialValues = createContentValues(url, color, shape, directable, type);
        return database.insertOrThrow(tableName, null, initialValues);
    }

    /**
     * Method to add a new row in the db, specifying the object that represents the entire db's tuple.
     * This cam be useful if you have the {@link Tile} already created and you don't want
     * to get all the fields and pass to the
     * {@link #insertTupleValues}.
     *
     * @param tableName The table name.
     * @param dbRow     The {@link Tile}.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertTupleObject(String tableName, Tile dbRow) {
        ContentValues initialValues = createContentValuesByObject(dbRow);
        return database.insertOrThrow(tableName, null, initialValues);
    }

    /**
     * Method to get the entire db.
     *
     * @param tableName The table name.
     * @return A Cursor.
     */
    public Cursor fetchAllData(String tableName) {
        return database.query(tableName, InternalConfig.ALL_COLUMNS,
                null, null, null, null, null);
    }

    /**
     * Method to fetch tuple's by a specific whereClause.
     *
     * @param tableName   The table name.
     * @param whereClause Where statement of the query.
     * @return A Cursor.
     */
    public Cursor fetchByQuery(String tableName, String whereClause) {
        Log.d("DbAdapter", whereClause);
        Cursor mCursor = database.query(true, tableName, null,
                whereClause, null, null, null, null, null);
        return mCursor;
    }


    private ContentValues createContentValues(String name, String color, String shape, boolean directable, String type) {
        ContentValues values = new ContentValues();
        values.put(InternalConfig.KEY_NAME, name);
        values.put(InternalConfig.KEY_COLOR, color);
        values.put(InternalConfig.KEY_SHAPE, shape);
        values.put(InternalConfig.KEY_DIRECTABLE, directable ? 1 : 0);
        values.put(InternalConfig.KEY_TYPE, type);
        return values;
    }

    private ContentValues createContentValuesByObject(Tile tile) {
        return this.createContentValues(tile.getName(), tile.getColor().toString(), tile.getShape().toString(),
                tile.isDirectable(), tile.getType().toString());
    }

}

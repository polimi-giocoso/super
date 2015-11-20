package it.playfellas.superapp.logic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.playfellas.superapp.InternalConfig;

/**
 * Class that creates and upgareds the db.
 */
class DbHelper extends SQLiteOpenHelper {

    // Creation statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + InternalConfig.TABLE_NAME + " ( " +
                    InternalConfig.KEY_ID + " integer primary key, " +
                    InternalConfig.KEY_NAME + " text not null, " +
                    InternalConfig.KEY_COLOR + " text not null, " +
                    InternalConfig.KEY_SHAPE + " text not null, " +
                    InternalConfig.KEY_DIRECTABLE + " integer not null, " + // boolean
                    InternalConfig.KEY_TYPE + " text not null" +
                    ")";


    public DbHelper(Context context) {
        super(context, InternalConfig.DATABASE_NAME, null, InternalConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + InternalConfig.TABLE_NAME);
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + InternalConfig.TABLE_NAME);
        onCreate(database);
    }
}
package it.playfellas.superapp.ui;

import android.content.Context;
import android.content.SharedPreferences;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.R;

/**
 * Created by affo on 10/09/15.
 */
public class FastStartPreferences {
    // app preferences
    public static final String APP_MASTER = "app_master";
    public static final String APP_PLAYER1 = "app_player1";
    public static final String APP_PLAYER2 = "app_player2";
    public static final String APP_PLAYER3 = "app_player3";
    public static final String APP_PLAYER4 = "app_player4";

    private static String[] playersPrefs = {
            FastStartPreferences.APP_PLAYER1,
            FastStartPreferences.APP_PLAYER2,
            FastStartPreferences.APP_PLAYER3,
            FastStartPreferences.APP_PLAYER4
    };

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(ctx.getString(R.string.preference_key_app), Context.MODE_PRIVATE);
    }

    public static boolean isMaster(Context ctx) {
        return prefs(ctx).getBoolean(APP_MASTER, false);
    }

    public static void setMaster(Context ctx, boolean ms) {
        prefs(ctx).edit().putBoolean(APP_MASTER, ms).apply();
    }

    public static String[] getPlayers(Context ctx) {
        String[] addresses = new String[InternalConfig.MAX_NO_PLAYERS];
        addresses[0] = prefs(ctx).getString(APP_PLAYER1, null);
        addresses[1] = prefs(ctx).getString(APP_PLAYER2, null);
        addresses[2] = prefs(ctx).getString(APP_PLAYER3, null);
        addresses[3] = prefs(ctx).getString(APP_PLAYER4, null);
        return addresses;
    }

    public static void resetPlayers(Context ctx) {
        prefs(ctx).edit().remove(APP_PLAYER1).apply();
        prefs(ctx).edit().remove(APP_PLAYER2).apply();
        prefs(ctx).edit().remove(APP_PLAYER3).apply();
        prefs(ctx).edit().remove(APP_PLAYER4).apply();
    }

    public static void savePlayer(Context ctx, int playerNumber, String address) {
        prefs(ctx).edit().putString(playersPrefs[playerNumber], address).apply();
    }
}

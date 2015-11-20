package it.playfellas.superapp.ui.slave;

import android.os.Build;

import java.util.Locale;

/**
 * Created by Stefano Cappa on 07/09/15.
 */

/**
 * To get the ocrrect orientation based on the device name.
 * Because, on Nexus 9 the camera is rotated by 180 degrees.
 */
public class PhotoUtils {

    public static int getFixedOrientationDegree() {
        if (getDeviceName().equals("HTC NEXUS 9")) {
            return 180;
        } else {
            return 0;
        }
    }

    //to fix a problem with Nexus 9
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + capitalize(model);
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        } else {
            return s.toUpperCase(new Locale(Locale.ENGLISH.getLanguage(), Locale.US.getCountry()));
        }
    }
}

package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "privacy_friendly_apps";

    private static final String IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";
    private static final String DB_PASSPHRASE = "dbPassphrase";

    private SharedPreferencesManager() {
    }

    public static void init(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public static boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void removeDbPassphrase() {
        editor.remove(DB_PASSPHRASE);
        editor.commit();
    }
    public static void setDbPassphrase(String passphrase) {
        editor.putString(DB_PASSPHRASE, passphrase);
        editor.commit();
    }
    public static String getDbPassphrase() {
        return pref.getString(DB_PASSPHRASE, null);
    }


}

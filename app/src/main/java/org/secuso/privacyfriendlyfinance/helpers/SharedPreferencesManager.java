/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Shared preference manager.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class SharedPreferencesManager {

    private static SharedPreferencesManager sharedPreferencesManager;

    public static final int PREF_MODE = Context.MODE_PRIVATE;
    public static final String PREF_NAME = "privacy_friendly_apps";

    public static final String KEY_IS_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";
    public static final String KEY_DB_PASSPHRASE = "dbPassphrase";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PREF_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_IS_FIRST_TIME_LAUNCH, true);
    }

    public void removeDbPassphrase() {
        editor.remove(KEY_DB_PASSPHRASE);
        editor.commit();
    }

    public void setDbPassphrase(String passphrase) {
        editor.putString(KEY_DB_PASSPHRASE, passphrase);
        editor.commit();
    }

    public String getDbPassphrase() {
        return pref.getString(KEY_DB_PASSPHRASE, null);
    }

    public static SharedPreferencesManager get(Context context) {
        if(sharedPreferencesManager == null) {
            synchronized (SharedPreferencesManager.class) {
                if(sharedPreferencesManager == null) {
                    // Use application context to prevent leaking specific context
                    sharedPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());
                }
            }
        }

        return sharedPreferencesManager;
    }
}

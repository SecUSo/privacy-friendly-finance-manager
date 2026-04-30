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

import org.joda.time.DateTimeConstants;

import java.util.Currency;
import java.util.Locale;

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
    public static final String KEY_DEFAULT_CURRENCY_CODE = "defaultCurrencyCode";
    private static final String KEY_RECENT_CURRENCIES = "recentCurrencies";
    private static final String KEY_ACCOUNT_CURRENCY_PREFIX = "accountCurrencyCode.";
    private static final String KEY_REPEATING_MONTH_DAY_PREFIX = "repeatingMonthDay.";
    private static final String KEY_REPEATING_WEEKDAY_PREFIX = "repeatingWeekday.";

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

    public void setDefaultCurrencyCode(String currencyCode) {
        String normalized = normalizeCurrencyCode(currencyCode);
        editor.putString(KEY_DEFAULT_CURRENCY_CODE, normalized);
        editor.commit();
        addRecentCurrencyCode(normalized);
    }

    public String getDefaultCurrencyCode() {
        return normalizeCurrencyCode(pref.getString(KEY_DEFAULT_CURRENCY_CODE, getLocaleCurrencyCode()));
    }

    public void setAccountCurrencyCode(long accountId, String currencyCode) {
        String normalized = normalizeCurrencyCode(currencyCode);
        editor.putString(KEY_ACCOUNT_CURRENCY_PREFIX + accountId, normalized);
        editor.commit();
        addRecentCurrencyCode(normalized);
    }

    public void removeAccountCurrencyCode(long accountId) {
        editor.remove(KEY_ACCOUNT_CURRENCY_PREFIX + accountId);
        editor.commit();
    }

    public String getAccountCurrencyCode(long accountId) {
        String fallback = getDefaultCurrencyCode();
        return normalizeCurrencyCode(pref.getString(KEY_ACCOUNT_CURRENCY_PREFIX + accountId, fallback));
    }

    public boolean hasAccountCurrencyCode(long accountId) {
        return pref.contains(KEY_ACCOUNT_CURRENCY_PREFIX + accountId);
    }

    public void addRecentCurrencyCode(String currencyCode) {
        String normalized = normalizeCurrencyCode(currencyCode);
        String oldCodes = pref.getString(KEY_RECENT_CURRENCIES, "");
        String[] parts = oldCodes == null ? new String[0] : oldCodes.split(",");
        StringBuilder updated = new StringBuilder(normalized);
        int count = 1;
        for (String part : parts) {
            if (part == null || part.trim().isEmpty()) continue;
            String code = normalizeCurrencyCode(part);
            if (code.equals(normalized)) continue;
            updated.append(",").append(code);
            count++;
            if (count >= 8) break;
        }
        editor.putString(KEY_RECENT_CURRENCIES, updated.toString());
        editor.commit();
    }

    public String[] getRecentCurrencyCodes() {
        String codes = pref.getString(KEY_RECENT_CURRENCIES, "");
        if (codes == null || codes.trim().isEmpty()) {
            return new String[0];
        }
        return codes.split(",");
    }

    public void setRepeatingMonthDay(long repeatingTransactionId, int dayOfMonth) {
        if (dayOfMonth < 1) {
            dayOfMonth = 1;
        } else if (dayOfMonth > 31) {
            dayOfMonth = 31;
        }
        editor.putInt(KEY_REPEATING_MONTH_DAY_PREFIX + repeatingTransactionId, dayOfMonth);
        editor.commit();
    }

    public int getRepeatingMonthDay(long repeatingTransactionId, int fallback) {
        int defaultValue = Math.min(Math.max(fallback, 1), 31);
        int value = pref.getInt(KEY_REPEATING_MONTH_DAY_PREFIX + repeatingTransactionId, defaultValue);
        return Math.min(Math.max(value, 1), 31);
    }

    public void removeRepeatingMonthDay(long repeatingTransactionId) {
        editor.remove(KEY_REPEATING_MONTH_DAY_PREFIX + repeatingTransactionId);
        editor.commit();
    }

    public void setRepeatingWeekDay(long repeatingTransactionId, int dayOfWeek) {
        if (dayOfWeek < DateTimeConstants.MONDAY || dayOfWeek > DateTimeConstants.SUNDAY) {
            dayOfWeek = DateTimeConstants.MONDAY;
        }
        editor.putInt(KEY_REPEATING_WEEKDAY_PREFIX + repeatingTransactionId, dayOfWeek);
        editor.commit();
    }

    public int getRepeatingWeekDay(long repeatingTransactionId, int fallback) {
        int defaultValue = fallback;
        if (defaultValue < DateTimeConstants.MONDAY || defaultValue > DateTimeConstants.SUNDAY) {
            defaultValue = DateTimeConstants.MONDAY;
        }
        int value = pref.getInt(KEY_REPEATING_WEEKDAY_PREFIX + repeatingTransactionId, defaultValue);
        if (value < DateTimeConstants.MONDAY || value > DateTimeConstants.SUNDAY) {
            return DateTimeConstants.MONDAY;
        }
        return value;
    }

    public void removeRepeatingWeekDay(long repeatingTransactionId) {
        editor.remove(KEY_REPEATING_WEEKDAY_PREFIX + repeatingTransactionId);
        editor.commit();
    }

    private String normalizeCurrencyCode(String currencyCode) {
        if (currencyCode != null) {
            String normalized = currencyCode.trim().toUpperCase(Locale.ROOT);
            try {
                Currency.getInstance(normalized);
                return normalized;
            } catch (IllegalArgumentException ignored) {
            }
        }
        return getLocaleCurrencyCode();
    }

    private String getLocaleCurrencyCode() {
        try {
            return Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        } catch (Exception ignored) {
            return "USD";
        }
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

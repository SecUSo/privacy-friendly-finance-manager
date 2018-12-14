package org.secuso.privacyfriendlyfinance.activities.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.DecimalFormat;

public class CurrencyTextWatcher implements TextWatcher {
    public static final DecimalFormat format = new DecimalFormat("#0.00");
    boolean mEditing = false;

    public static String convertToString(Long value) {
        if (value == null) return null;
        return format.format(value.doubleValue() / 100.00);
    }
    public static String convertToString(Double value) {
        if (value == null) return null;
        return format.format(value);
    }
    private static String digitsOf(String input) {
        return input.replaceAll("\\D", "");
    }

    public static Long convertToLong(String text) {
        try {
            return Long.parseLong(digitsOf(text));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Double convertToDouble(String text) {
        try {
            return Double.parseDouble(digitsOf(text)) / 100.0;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {
        if (!mEditing) {
            mEditing = true;
            String out = convertToString(convertToLong(s.toString()));
            Log.d("watcher", out);
            if (out != null) {
                s.replace(0, s.length(), out);
            } else {
                s.clear();
            }
            mEditing = false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

}
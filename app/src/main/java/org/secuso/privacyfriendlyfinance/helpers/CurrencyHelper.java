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
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public final class CurrencyHelper {
    public static final DecimalFormat format = new DecimalFormat("#0.00");
    private CurrencyHelper() {}

    public static void setBalance(Long balance, TextView textView, boolean setColor) {
        Context context = textView.getContext();
        if (balance == null)  balance = 0L;
        String prefix = setColor && balance > 0 ? "+" : "";
        textView.setText(prefix + NumberFormat.getCurrencyInstance().format(balance.doubleValue() / 100.0));
        if (setColor) {
            if (balance < 0) {
                textView.setTextColor(context.getResources().getColor(R.color.red));
            } else if (balance > 0) {
                textView.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                textView.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
            }
        }
    }

    public static void setBalance(Long balance, TextView textView) {
        setBalance(balance, textView, true);
    }

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
            return ((Double) (Double.parseDouble(text) * 100)).longValue();
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

    public static String convertToCurrencyString(Long amount) {
        return NumberFormat.getCurrencyInstance().format(amount.doubleValue() / 100.0);
    }
}

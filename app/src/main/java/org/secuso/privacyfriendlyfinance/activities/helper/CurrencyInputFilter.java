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

package org.secuso.privacyfriendlyfinance.activities.helper;


import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Input filter that filters currencies.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class CurrencyInputFilter implements InputFilter {
    Pattern pattern = Pattern.compile("-?[0-9]+((\\.[0-9]{0,2})?)||(\\.)?");
    private Double min;
    private Double max;

    public CurrencyInputFilter() {}
    public CurrencyInputFilter(Double min, Double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//        Log.d("CurrencyFilter", "filter " + source + " " + start + " " + end + " dest " + dest + " " + dstart + " " + dend);
        if (source.length() == 0) return null;

        String oldValue = dest.toString();
        String result = oldValue.substring(0, dstart) + source.toString().substring(start, end) + oldValue.substring(dend, oldValue.length());
        if (result.equals("-") && (min == null || min < 0.0)) return null;

        Matcher matcher = pattern.matcher(result);
        Log.d("CurrencyFilter", result + " " + matcher.matches());

        if (!matcher.matches()) return "";

        try {
            Double input = Double.parseDouble(result);
            if (Math.abs(input) >= 1_000_000_00L) return "";
            if ((min != null && input < min) || (max != null && input > max)) return "";

        } catch (NumberFormatException nfe) { }

        return null;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
}

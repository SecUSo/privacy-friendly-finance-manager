package org.secuso.privacyfriendlyfinance.activities.helper;


import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Matcher matcher = pattern.matcher(result);
        Log.d("CurrencyFilter", result + " " + matcher.matches());

        if (!matcher.matches()) return "";

        try {
            Double input = Double.parseDouble(result);
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

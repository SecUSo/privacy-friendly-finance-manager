package org.secuso.privacyfriendlyfinance.activities.helper;

import android.text.Editable;
import android.text.TextWatcher;

import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

public class CurrencyTextWatcher implements TextWatcher {
    boolean editing = false;

    @Override
    public synchronized void afterTextChanged(Editable s) {
        if (!editing) {
            editing = true;
            String out = CurrencyHelper.convertToString(CurrencyHelper.convertToLong(s.toString()));
            if (out != null) {
                s.replace(0, s.length(), out);
            } else {
                s.clear();
            }
            editing = false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

}
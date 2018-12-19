package org.secuso.privacyfriendlyfinance.helpers;

import android.content.Context;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

import java.text.NumberFormat;

public final class CurrencyHelper {
    private CurrencyHelper() {}

    public static void setBalance(Long balance, TextView textView) {
        Context context = textView.getContext();
        if (balance == null)  balance = 0L;
        textView.setText(NumberFormat.getCurrencyInstance().format(balance.doubleValue() / 100.0));
        if (balance < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.red));
        } else if (balance > 0) {
            textView.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            textView.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        }
    }
}

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

import java.text.NumberFormat;

public abstract class AbstractRecyclerViewHolder extends RecyclerView.ViewHolder {
    protected final Context context;

    public AbstractRecyclerViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

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

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

public class RepeatingTransactionViewHolder extends TransactionViewHolder {
    private TextView tvRepeating;

    public RepeatingTransactionViewHolder(@NonNull View itemView, Context context) {
        super(itemView, context);
        tvRepeating = itemView.findViewById(R.id.textView_repeating);
    }

    public void setRepeatingText(String repeatingText) {
        tvRepeating.setText(repeatingText);
    }
}

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

import java.util.Locale;

public class TransactionViewHolder extends AbstractRecyclerViewHolder {
    private TextView tvName;
    private TextView tvAmount;
    private TextView tvAccount;
    private TextView tvCategory;
    private ImageView ivCategory;
    private TextView tvRepeating;
    private ImageView ivRepeating;
    private TextView tvMonth;
    private TextView tvDay;


    public TransactionViewHolder(@NonNull View itemView, Context context) {
        super(itemView, context);
        tvName = itemView.findViewById(R.id.textview_transaction_name);
        tvAmount = itemView.findViewById(R.id.textView_amount);
        tvAccount = itemView.findViewById(R.id.textView_account);
        tvCategory = itemView.findViewById(R.id.textView_category);
        ivCategory = itemView.findViewById(R.id.imageView_category);
        tvRepeating = itemView.findViewById(R.id.textView_repeating);
        ivRepeating = itemView.findViewById(R.id.imageView_repeating);
        tvMonth = itemView.findViewById(R.id.textView_month);
        tvDay = itemView.findViewById(R.id.textView_day_of_month);
        setCategoryName(null);
    }

    public void setTransactionName(String name) {
        tvName.setText(name);
    }

    public void setAmount(Long amount) {
        CurrencyHelper.setBalance(amount, tvAmount);
    }

    public void setAccountName(String name) {
        tvAccount.setText(name);
    }

    public void setCategoryName(String name) {
        if (name == null) {
            tvCategory.setVisibility(View.INVISIBLE);
            ivCategory.setVisibility(View.INVISIBLE);
        } else {
            tvCategory.setVisibility(View.VISIBLE);
            ivCategory.setVisibility(View.VISIBLE);
        }
        tvCategory.setText(name);
    }
    public void setCategoryColor(Integer color) {
        if (color != null) {
            ivCategory.setColorFilter(color);
        } else {
            ivCategory.clearColorFilter();
        }
    }

    public void setRepeatingName(String repeatingName) {
        if (repeatingName == null) {
            tvRepeating.setVisibility(View.INVISIBLE);
            ivRepeating.setVisibility(View.INVISIBLE);
        } else if (repeatingName == tvName.getText()) {
            ivRepeating.setVisibility(View.VISIBLE);
            tvRepeating.setVisibility(View.INVISIBLE);
        } else {
            tvRepeating.setVisibility(View.VISIBLE);
            ivRepeating.setVisibility(View.VISIBLE);
        }
        tvRepeating.setText(repeatingName);
    }

    public void setDate(LocalDate date) {
        tvMonth.setText(date.toString("MMM", Locale.getDefault()));
        tvDay.setText(date.toString("dd"));
    }
}

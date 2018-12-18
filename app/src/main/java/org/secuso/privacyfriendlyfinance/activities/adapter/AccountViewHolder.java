package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

import java.text.NumberFormat;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private TextView tvAccountName;
    private TextView tvAccountBalance;
    private TextView tvAccountBalanceMonth;

    public AccountViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        tvAccountName = itemView.findViewById(R.id.textView_account_name);
        tvAccountBalance = itemView.findViewById(R.id.textView_balance);
        tvAccountBalanceMonth = itemView.findViewById(R.id.textView_balance_month_start);
    }

    protected void setBalance(Long balance, TextView textView) {
        if (balance == null)  balance = 0L;
        textView.setText(currencyFormat.format(balance.doubleValue() / 100.0));
        if (balance < 0) {
            textView.setTextColor(context.getResources().getColor(R.color.red));
        } else if (balance > 0) {
            textView.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            textView.setTextColor(context.getResources().getColor(android.R.color.tab_indicator_text));
        }
    }

    public void setBalance(Long balance) {
        setBalance(balance, tvAccountBalance);
    }
    public void setBalanceMonthStart(Long balance) {
        setBalance(balance, tvAccountBalanceMonth);
    }

    public void setAccountName(String name) {
        tvAccountName.setText(name);
    }
}

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

public class AccountViewHolder extends AbstractRecyclerViewHolder {
    private TextView tvAccountName;
    private TextView tvAccountBalance;
    private TextView tvAccountBalanceMonth;

    public AccountViewHolder(@NonNull View itemView, Context context) {
        super(itemView, context);
        tvAccountName = itemView.findViewById(R.id.textView_account_name);
        tvAccountBalance = itemView.findViewById(R.id.textView_balance);
        tvAccountBalanceMonth = itemView.findViewById(R.id.textView_balance_month_start);
    }

    public void setAccountName(String name) {
        tvAccountName.setText(name);
    }
    public void setBalance(Long balance) {
        CurrencyHelper.setBalance(balance, tvAccountBalance);
    }
    public void setBalanceMonthStart(Long balance) {
        CurrencyHelper.setBalance(balance, tvAccountBalanceMonth);
    }
}

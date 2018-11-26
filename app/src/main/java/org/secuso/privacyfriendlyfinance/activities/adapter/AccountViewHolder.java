package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAccountName;
    private TextView tvAccountBalanceCurrent;
    private TextView tvAccountBalanceMonth;

    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);
        tvAccountName = itemView.findViewById(R.id.account_card_name);
        tvAccountBalanceCurrent = itemView.findViewById(R.id.account_card_balance_current);
        tvAccountBalanceMonth = itemView.findViewById(R.id.acount_card_balance_month);
    }

    public TextView getTvAccountName() {
        return tvAccountName;
    }
    public void setTvAccountName(TextView tvAccountName) {
        this.tvAccountName = tvAccountName;
    }

    public TextView getTvAccountBalanceCurrent() {
        return tvAccountBalanceCurrent;
    }
    public void setTvAccountBalanceCurrent(TextView tvAccountBalanceCurrent) {
        this.tvAccountBalanceCurrent = tvAccountBalanceCurrent;
    }

    public TextView getTvAccountBalanceMonth() {
        return tvAccountBalanceMonth;
    }
    public void setTvAccountBalanceMonth(TextView tvAccountBalanceMonth) {
        this.tvAccountBalanceMonth = tvAccountBalanceMonth;
    }
}

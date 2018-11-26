package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAccountName;
    private TextView tvAccountBalance;

    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);
        tvAccountName = itemView.findViewById(R.id.account_card_name);
        tvAccountBalance = itemView.findViewById(R.id.account_card_balance);
    }

    public TextView getTvAccountName() {
        return tvAccountName;
    }
    public void setTvAccountName(TextView tvAccountName) {
        this.tvAccountName = tvAccountName;
    }

    public TextView getTvAccountBalance() {
        return tvAccountBalance;
    }
    public void setTvAccountBalance(TextView tvAccountBalance) {
        this.tvAccountBalance = tvAccountBalance;
    }
}

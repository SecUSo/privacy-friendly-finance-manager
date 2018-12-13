package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

public class AccountsAdapter extends EntityListAdapter<Account, AccountViewHolder> {
    public AccountsAdapter(BaseActivity context, LiveData<List<Account>> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_card_account, parent, false);
        return new AccountViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        Account account = getItem(index);
        holder.getTvAccountName().setText(account.getName());
//        holder.getTvAccountBalanceCurrent().setText(String.valueOf(account.getInitialBalance()));
        holder.getTvAccountBalanceMonth().setText(String.valueOf(42));
    }
}

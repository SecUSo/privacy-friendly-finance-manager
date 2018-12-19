package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;

import java.util.List;

public class AccountsAdapter extends EntityListAdapter<AccountWrapper, AccountViewHolder> {
    public AccountsAdapter(BaseActivity context, LiveData<List<AccountWrapper>> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_account, parent, false);
        return new AccountViewHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final AccountViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        AccountWrapper wrapper = getItem(index);
        holder.setAccountName(wrapper.getAccount().getName());

        wrapper.getCurrentBalance().observe(context, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long balance) {
                holder.setBalance(balance);
            }
        });

        wrapper.getStartOfMonthBalance().observe(context, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long balance) {
                holder.setBalanceMonthStart(balance);
            }
        });
    }
}

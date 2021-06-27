/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;

import java.util.List;

/**
 * Adapter for account lists.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
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

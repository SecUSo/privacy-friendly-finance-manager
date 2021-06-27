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

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

/**
 * View holder for an account. Connects the adapter to the view.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
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

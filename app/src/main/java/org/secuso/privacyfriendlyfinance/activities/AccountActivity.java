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

package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.dialog.AccountDialog;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

/**
 * Account activity. Used to show detailed information about an account and all transactions that
 * are linked to this account.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class AccountActivity extends TransactionListActivity {
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";
    protected AccountViewModel viewModel;

    @Override
    protected AccountViewModel getViewModel() {
        long accountId = getIntent().getLongExtra(EXTRA_ACCOUNT_ID, -1);
        AccountViewModel.AccountViewModelFactory viewModelFactory = new AccountViewModel.AccountViewModelFactory(this.getApplication(), accountId);
        return new ViewModelProvider(this, viewModelFactory).get(AccountViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (AccountViewModel) super.viewModel;
        viewModel.getAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                viewModel.setTitle(account.getName());
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addEditMenuClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openAccountDialog();

                return true;
            }
        });

        View view = setHeaderLayout(R.layout.header_account_balance);
        final TextView tvTotalBalance = view.findViewById(R.id.tv_totalBalance);
        final TextView tvMonthBalance = view.findViewById(R.id.tv_monthBalance);

        viewModel.getTotalBalance().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long totalBalance) {
                CurrencyHelper.setBalance(totalBalance, tvTotalBalance);
            }
        });

        viewModel.getMonthBalance().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long monthBalance) {
                CurrencyHelper.setBalance(monthBalance, tvMonthBalance);
            }
        });
    }

    private void openAccountDialog() {
        AccountDialog.showAccountDialog(
                viewModel.getAccount().getValue(),
                viewModel.getMonthBalance().getValue(),
                getSupportFragmentManager());
    }

    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return AccountViewModel.class;
    }
}

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

package org.secuso.privacyfriendlyfinance.activities.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountDialogViewModel;
import org.secuso.privacyfriendlyfinance.databinding.DialogAccountBinding;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog used to create and edit accounts.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class AccountDialog extends AppCompatDialogFragment {
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";
    public static final String EXTRA_ACCOUNT_MONTH_BALANCE = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_MONTH_BALANCE";

    private AccountDialogViewModel viewModel;
    private Account accountObject;
    private List<Account> allAccounts = new ArrayList<Account>();
    private boolean editingExistingAccount;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(AccountDialogViewModel.class);
        viewModel.setInitialMonthBalance(getArguments().getLong(EXTRA_ACCOUNT_MONTH_BALANCE, 0L));
        viewModel.setCurrencyColors(getResources().getColor(R.color.green), getResources().getColor(R.color.red));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final DialogAccountBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_account, null, false);
        View view = binding.getRoot();
        viewModel.setAccountId(getArguments().getLong(EXTRA_ACCOUNT_ID, -1L)).observe(this, new Observer<Account>() {
            @Override
            public void onChanged(@Nullable Account account) {
                viewModel.setAccount(account);
                binding.setViewModel(viewModel);
            }
        });
        builder.setView(view);

        builder.setTitle(getArguments().getLong(EXTRA_ACCOUNT_ID, -1L) == -1L ? R.string.dialog_account_create_title : R.string.dialog_account_edit_title);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                viewModel.cancel();
            }
        });

        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                viewModel.submit(getString(R.string.compensation_transaction_default_name));
            }
        });

        return builder.create();
    }



    protected void onClickPositive(String textFromTextInput) {
        String accountName = textFromTextInput.trim();
        if (accountName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.dialog_account_empty_name_impossible_msg), Toast.LENGTH_LONG).show();
        } else if (isAccountNameTaken(accountName)) {
            Toast.makeText(getContext(), getString(R.string.dialog_account_name_taken_msg), Toast.LENGTH_LONG).show();
        } else {
            if (accountObject == null) {
                //We are working on a new account object and are NOT editing an existing one
                accountObject = new Account(accountName);
            } else {
                accountObject.setName(accountName);
            }

            viewModel.updateOrInsert(accountObject);

            Toast.makeText(getContext(), R.string.account_saved_msg, Toast.LENGTH_SHORT).show();

            dismiss();
        }
    }

    private boolean isAccountNameTaken(String accountName) {
        synchronized (allAccounts) {
            if (allAccounts == null || allAccounts.isEmpty()) {
                return true;
            }
            for (int i = 0; i < allAccounts.size(); i++) {
                if (allAccounts.get(i).getName().toLowerCase().equals(accountName.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    }
}

package org.secuso.privacyfriendlyfinance.activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountDialogViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDialog extends SimpleTextInputDialog {
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";

    private AccountDialogViewModel viewModel;
    private Account accountObject;
    private List<Account> allAccounts = new ArrayList<Account>();

    @Override
    protected void retrieveData() {
        viewModel = ViewModelProviders.of(this).get(AccountDialogViewModel.class);

        Bundle args = getArguments();
        long accountId = args.getLong(EXTRA_ACCOUNT_ID, -1L);
        if (accountId == -1L) {
        } else {
            viewModel.getAccountById(accountId).observe(this, new Observer<Account>() {
                @Override
                public void onChanged(@Nullable Account account) {
                    editTextInput.setText(account.getName());
                    accountObject = account;
                }
            });
        }
        viewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts) {
                synchronized (allAccounts) {
                    allAccounts = accounts;
                }
            }
        });
    }

    @Override
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

    @Override
    protected String getTextInputHint() {
        return getResources().getString(R.string.dialog_account_name_hint);
    }
}

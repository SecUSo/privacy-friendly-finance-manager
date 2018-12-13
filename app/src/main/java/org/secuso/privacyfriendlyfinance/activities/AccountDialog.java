package org.secuso.privacyfriendlyfinance.activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountDialogViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountDialog extends SimpleTextInputDialog {
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";

    private AccountDialogViewModel viewModel;

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
                }
            });
        }
    }

    @Override
    protected void onClickPositive(String textFromTextInput) {
        String accountName = textFromTextInput.trim();
        if (accountName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.dialog_category_empty_name_impossible_msg), Toast.LENGTH_LONG).show();
        } else {
            Account tmpAccount = new Account(accountName, 0L);

            viewModel.updateOrInsert(tmpAccount);

            Toast.makeText(getContext(), R.string.activity_transaction_saved_msg, Toast.LENGTH_SHORT).show();

            //TODO: Is this really done like this?
            Intent intent = new Intent(getActivity(), CategoriesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected String getTextInputHint() {
        return getResources().getString(R.string.dialog_account_name_hint);
    }
}

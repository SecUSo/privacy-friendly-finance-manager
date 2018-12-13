package org.secuso.privacyfriendlyfinance.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.MenuItem;

import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountActivity extends TransactionListActivity {
    public static final String EXTRA_ACCOUNT_ID = "org.secuso.privacyfriendlyfinance.EXTRA_ACCOUNT_ID";
    protected AccountViewModel viewModel;

    @Override
    protected AccountViewModel getViewModel() {
        long accountId = getIntent().getLongExtra(EXTRA_ACCOUNT_ID, -1);
        AccountViewModel.AccountViewModelFactory viewModelFactory = new AccountViewModel.AccountViewModelFactory(this.getApplication(), accountId);
        return ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel.class);
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
    }

    private void openAccountDialog() {
        AccountDialog accountDialog = new AccountDialog();

        Bundle args = new Bundle();
        args.putLong(AccountDialog.EXTRA_ACCOUNT_ID, viewModel.getAccount().getValue().getId());
        accountDialog.setArguments(args);

        accountDialog.show(getSupportFragmentManager(), "AccountDialog");
    }

    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return AccountViewModel.class;
    }
}

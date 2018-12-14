package org.secuso.privacyfriendlyfinance.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountWrapper;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountsAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountsViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends BaseActivity implements OnItemClickListener<AccountWrapper> {
    private AccountsViewModel viewModel;
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    @Override
    protected Class<? extends BaseViewModel> getViewModelClass() {
        return AccountsViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = (AccountsViewModel) super.viewModel;


        final MutableLiveData<List<AccountWrapper>> accountWrappers = new MutableLiveData<>();

        accountsAdapter = new AccountsAdapter(this, accountWrappers);
        viewModel.getAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> accounts) {
                List<AccountWrapper> wrappers = new ArrayList<>();
                for (Account account : accounts) {
                    wrappers.add(new AccountWrapper(account));
                }
                accountWrappers.postValue(wrappers);
            }
        });

        accountsAdapter.onItemClick(this);

        setContent(R.layout.content_recycler);
        addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountDialog(null);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountsAdapter);
    }

    private void openAccountDialog(Account account) {
        Bundle args = new Bundle();
        if (account == null) {
        } else {
            args.putLong(CategoryDialog.EXTRA_CATEGORY_ID, account.getId());
        }

        AccountDialog accountDialog = new AccountDialog();
        accountDialog.setArguments(args);

        accountDialog.show(getSupportFragmentManager(), "CategoryDialog");
    }

    @Override
    public void onItemClick(AccountWrapper item) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT_ID, item.getId());
        startActivity(intent);
    }
}

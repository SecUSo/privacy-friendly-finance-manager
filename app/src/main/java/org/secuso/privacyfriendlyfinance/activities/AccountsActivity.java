package org.secuso.privacyfriendlyfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountsAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountsViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountsActivity extends BaseActivity implements OnItemClickListener<Account> {
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
        accountsAdapter = new AccountsAdapter(this, viewModel.getAccounts());
        accountsAdapter.onItemClick(this);

        setContent(R.layout.content_recycler);
        addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountsAdapter);
    }

    private void addAccount() {
    }

    @Override
    public void onItemClick(Account item) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT_ID, item.getId());
        startActivity(intent);
    }
}

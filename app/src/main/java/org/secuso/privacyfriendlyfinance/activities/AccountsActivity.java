package org.secuso.privacyfriendlyfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountsAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountsActivity extends BaseActivity implements OnItemClickListener<Account> {
    private AccountDao dao = FinanceDatabase.getInstance().accountDao();
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accounts);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        makeRecyclerView();

        findViewById(R.id.fab_addaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
            }
        });
    }

    private void makeRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);

        accountsAdapter = new AccountsAdapter(this, dao.getAll());
        accountsAdapter.onItemClick(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountsAdapter);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_account;
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

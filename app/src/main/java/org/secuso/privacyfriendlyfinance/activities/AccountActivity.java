package org.secuso.privacyfriendlyfinance.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountsAdapter;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends BaseActivity implements TaskListener {
    private RecyclerView recyclerView;
    private List<Account> accountList;
    private AccountsAdapter accountsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addaccount);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccount();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void makeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        accountList = new ArrayList<Account>();

        accountsAdapter = new AccountsAdapter(this, accountList);

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
    public void onDone(Object result, AsyncTask<?, ?, ?> task) {

    }

    @Override
    public void onProgress(Double progress, AsyncTask<?, ?, ?> task) {

    }

    @Override
    public void onOperation(String operation, AsyncTask<?, ?, ?> task) {

    }
}

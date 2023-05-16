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

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountWrapper;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountsAdapter;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.dialog.AccountDialog;
import org.secuso.privacyfriendlyfinance.activities.helper.SwipeController;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.AccountsViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

/**
 * The accounts activity shows all accounts and provides the possibility to create new accounts.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
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

        accountsAdapter = new AccountsAdapter(this, viewModel.getAccounts());

        accountsAdapter.onItemClick(this);

        setContent(R.layout.content_recycler);
        addFab(R.layout.fab_add, view -> openAccountDialog());

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountsAdapter);

        SwipeController.SwipeControllerAction deleteAction = new SwipeController.SwipeControllerAction() {
            @Override
            public void onClick(int position) {
                deleteAccount(viewModel.getAccounts().getValue().get(position).getAccount());
            }
            @Override
            public Drawable getIcon() {
                return ContextCompat.getDrawable(AccountsActivity.this, R.drawable.ic_delete_red_24dp);
            }
        };

        final SwipeController swipeController = new SwipeController(this, deleteAction, deleteAction);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void deleteAccount(final Account account) {
        if (viewModel.getAccounts().getValue().size() > 1) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.account_delete_action)
                    .setMessage(HtmlCompat.fromHtml(getResources().getString(R.string.account_delete_question, account.getName()), HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(R.string.delete, (dialog, id) -> {
                        FinanceDatabase.getInstance(this).accountDao().deleteAsync(account);
                        Toast.makeText(getBaseContext(), R.string.account_deleted_msg, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {})
                    .create().show();
        } else {
            Toast.makeText(getBaseContext(), R.string.account_last_not_deleteable, Toast.LENGTH_LONG).show();
        }
    }

    private void openAccountDialog() {
        AccountDialog.showAccountDialog(null, null, getSupportFragmentManager());
    }

    @Override
    public void onItemClick(AccountWrapper item) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT_ID, item.getId());
        startActivity(intent);
    }
}

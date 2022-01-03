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
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.adapter.RepeatingTransactionsAdapter;
import org.secuso.privacyfriendlyfinance.activities.dialog.RepeatingTransactionDialog;
import org.secuso.privacyfriendlyfinance.activities.helper.SwipeController;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.RepeatingTransactionsViewModel;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

/**
 * This activity shows all repeating transactions. Provides the possibility to create new repeating
 * transactions.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class RepeatingTransactionsActivity extends BaseActivity implements OnItemClickListener<RepeatingTransaction> {
    private RepeatingTransactionsAdapter repeatingTransactionsAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected Class<? extends BaseViewModel> getViewModelClass() {
        return RepeatingTransactionsViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent(R.layout.content_recycler);

        final RepeatingTransactionsViewModel tmpViewModel = (RepeatingTransactionsViewModel) super.viewModel;
        repeatingTransactionsAdapter = new RepeatingTransactionsAdapter(this, tmpViewModel.getRepeatingTransactions());
        repeatingTransactionsAdapter.onItemClick(this);

        addFab(R.layout.fab_add, v -> openRepeatingTransactionDialog(null));

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repeatingTransactionsAdapter);

        SwipeController.SwipeControllerAction deleteAction = new SwipeController.SwipeControllerAction() {
            @Override
            public void onClick(int position) {
                deleteRepeatingTransaction(tmpViewModel.getRepeatingTransactions().getValue().get(position));
            }
            @Override
            public Drawable getIcon() {
                return ContextCompat.getDrawable(RepeatingTransactionsActivity.this, R.drawable.ic_delete_red_24dp);
            }
        };

        final SwipeController swipeController = new SwipeController(this, deleteAction, deleteAction);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        emptyView = findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.activity_transactions_empty_list_label));
        tmpViewModel.getRepeatingTransactions().observe(this, repeatingTransactions -> {
            if (repeatingTransactions.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        });
    }

    private void deleteRepeatingTransaction(final RepeatingTransaction transaction) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.repeat_delete_action)
                .setMessage(HtmlCompat.fromHtml(getResources().getString(R.string.repeat_delete_question, transaction.getName()), HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setPositiveButton(R.string.delete, (dialog, id) -> {
                    FinanceDatabase.getInstance().repeatingTransactionDao().deleteAsync(transaction);
                    Toast.makeText(getBaseContext(), R.string.repeat_deleted_msg, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {})
                .create().show();
    }

    private void openRepeatingTransactionDialog(RepeatingTransaction repeatingTransaction) {
        Bundle args = new Bundle();
        if (repeatingTransaction != null) {
            args.putLong(RepeatingTransactionDialog.EXTRA_TRANSACTION_ID, repeatingTransaction.getId());
        }

        RepeatingTransactionDialog dialog = new RepeatingTransactionDialog();
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "RepeatingTransactionDialog");
    }

    @Override
    public void onItemClick(RepeatingTransaction item) {
        openRepeatingTransactionDialog(item);
    }
}

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
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.adapter.TransactionsAdapter;
import org.secuso.privacyfriendlyfinance.activities.dialog.TransactionDialog;
import org.secuso.privacyfriendlyfinance.activities.helper.SwipeController;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.databinding.ContentTransactionListBinding;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

/**
 * This abstract class is provided as a base class for all
 * activities that show a list of transactions. Classes that use
 * this class as a super class are: TransactionsActivity, AccountActivity,
 * CategoryActivity...
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public abstract class TransactionListActivity extends BaseActivity implements OnItemClickListener<Transaction> {
    private RecyclerView recyclerView;
    private TextView emptyView;
    protected TransactionListViewModel viewModel;

    protected abstract Class<? extends TransactionListViewModel> getViewModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (TransactionListViewModel) super.viewModel;
        ContentTransactionListBinding binding = DataBindingUtil.bind(setContent(R.layout.content_transaction_list));
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(null);
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TransactionsAdapter adapter = new TransactionsAdapter(this, viewModel.getTransactions());
        adapter.onItemClick(this);
        recyclerView.setAdapter(adapter);

        emptyView = findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.activity_transactions_empty_list_label));
        viewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                if (transactions.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });


        SwipeController.SwipeControllerAction deleteAction = new SwipeController.SwipeControllerAction() {
            @Override
            public void onClick(int position) {
                deleteTransaction(viewModel.getTransactions().getValue().get(position));
            }
            @Override
            public Drawable getIcon() {
                return ContextCompat.getDrawable(TransactionListActivity.this, R.drawable.ic_delete_red_24dp);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    protected View setHeaderLayout(@LayoutRes int layoutResId) {
        LinearLayout linearLayout = findViewById(R.id.linear_layout_root);

        View view = getLayoutInflater().inflate(layoutResId, null, false);
        linearLayout.addView(view, 0);

        RelativeLayout separator = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        separator.setLayoutParams(params);
        separator.setMinimumHeight(5);
        separator.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        linearLayout.addView(separator, 1);

        return view;
    }

    @Override
    protected final void onResume() {
        super.onResume();
    }

    private void deleteTransaction(final Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_transaction_title)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FinanceDatabase.getInstance().transactionDao().deleteAsync(transaction);
                        Toast.makeText(TransactionListActivity.this, R.string.activity_transaction_deleted_msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(Transaction item) {
        TransactionDialog transactionDialog = new TransactionDialog();
        Bundle args = new Bundle();
        if (item != null) {
            args.putLong(TransactionDialog.EXTRA_TRANSACTION_ID, item.getId());
        } else {
            args.putLong(TransactionDialog.EXTRA_CATEGORY_ID, viewModel.getPreselectedCategoryId());
            args.putLong(TransactionDialog.EXTRA_ACCOUNT_ID, viewModel.getPreselectedAccountId());
        }
        transactionDialog.setArguments(args);
        transactionDialog.show(getSupportFragmentManager(), "TransactionDialog");
    }
}

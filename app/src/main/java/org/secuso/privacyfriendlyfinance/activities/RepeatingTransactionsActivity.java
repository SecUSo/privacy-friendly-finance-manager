package org.secuso.privacyfriendlyfinance.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.adapter.RepeatingTransactionsAdapter;
import org.secuso.privacyfriendlyfinance.activities.dialog.RepeatingTransactionDialog;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.RepeatingTransactionsViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

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

        RepeatingTransactionsViewModel tmpViewModel = (RepeatingTransactionsViewModel) super.viewModel;
        repeatingTransactionsAdapter = new RepeatingTransactionsAdapter(this, tmpViewModel.getRepeatingTransactions());
        repeatingTransactionsAdapter.onItemClick(this);

        addFab(R.layout.fab_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRepeatingTransactionDialog(null);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repeatingTransactionsAdapter);

        emptyView = findViewById(R.id.empty_view);
        emptyView.setText(getString(R.string.activity_transactions_empty_list_label));
        tmpViewModel.getRepeatingTransactions().observe(this, new Observer<List<RepeatingTransaction>>() {
            @Override
            public void onChanged(@Nullable List<RepeatingTransaction> repeatingTransactions) {
                if (repeatingTransactions.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void openRepeatingTransactionDialog(RepeatingTransaction repeatingTransaction) {
        Bundle args = new Bundle();
        if (repeatingTransaction == null) {

        } else {
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

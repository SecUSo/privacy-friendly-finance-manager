package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.OnItemClickListener;
import org.secuso.privacyfriendlyfinance.activities.adapter.RepeatingTransactionsAdapter;
import org.secuso.privacyfriendlyfinance.activities.dialog.RepeatingTransactionDialog;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.BaseViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.RepeatingTransactionsViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

public class RepeatingTransactionsActivity extends BaseActivity implements OnItemClickListener<RepeatingTransaction> {
    private RepeatingTransactionsAdapter repeatingTransactionsAdapter;
    private RecyclerView recyclerView;

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
    }

    private void openRepeatingTransactionDialog(RepeatingTransaction repeatingTransaction) {
        Bundle args = new Bundle();
        if (repeatingTransaction == null) {

        } else {
            args.putLong(RepeatingTransactionDialog.EXTRA_ACCOUNT_ID, repeatingTransaction.getAccountId());
            args.putLong(RepeatingTransactionDialog.EXTRA_CATEGORY_ID, repeatingTransaction.getCategoryId());
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

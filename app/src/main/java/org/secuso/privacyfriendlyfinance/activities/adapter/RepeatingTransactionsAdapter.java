package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

public class RepeatingTransactionsAdapter extends EntityListAdapter<RepeatingTransaction, RepeatingTransactionViewHolder> {
    public RepeatingTransactionsAdapter(BaseActivity context, LiveData<List<RepeatingTransaction>> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public RepeatingTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_repeating_transaction, parent, false);
        return new RepeatingTransactionViewHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RepeatingTransactionViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        RepeatingTransaction rt = getItem(index);
    }
}

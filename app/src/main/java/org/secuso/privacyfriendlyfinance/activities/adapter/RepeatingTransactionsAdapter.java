package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;
import org.secuso.privacyfriendlyfinance.helpers.RepeatingHelper;

import java.util.List;
import java.util.Map;

public class RepeatingTransactionsAdapter extends EntityListAdapter<RepeatingTransaction, RepeatingTransactionViewHolder> {
    private LiveData<Map<Long, Account>> accounts = FinanceDatabase.getInstance().accountDao().getCacheMap();
    private LiveData<Map<Long, Category>> categories = FinanceDatabase.getInstance().categoryDao().getCacheMap();


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
    public void onBindViewHolder(@NonNull final RepeatingTransactionViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        final RepeatingTransaction rt = getItem(index);
        holder.setAmount(rt.getAmount());
        holder.setTransactionName(rt.getName());
        holder.setRepeatingText(RepeatingHelper.forgeRepeatingText(context, rt));

        accounts.observe(context, new Observer<Map<Long, Account>>() {
            @Override
            public void onChanged(@Nullable Map<Long, Account> map) {
                holder.setAccountName(map.get(rt.getAccountId()).getName());
            }
        });

        categories.observe(context, new Observer<Map<Long, Category>>() {
            @Override
            public void onChanged(@Nullable Map<Long, Category> map) {
                if (rt.getCategoryId() != null) {
                    Category category = map.get(rt.getCategoryId());
                    holder.setCategoryName(category.getName());
                    holder.setCategoryColor(category.getColor());
                } else {
                    holder.setCategoryName(null);
                    holder.setCategoryColor(null);
                }
            }
        });
    }
}

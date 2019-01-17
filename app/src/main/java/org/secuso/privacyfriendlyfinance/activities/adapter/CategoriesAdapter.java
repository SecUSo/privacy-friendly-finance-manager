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
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

public class CategoriesAdapter extends EntityListAdapter<CategoryWrapper, CategoryViewHolder> {
    public CategoriesAdapter(BaseActivity context, LiveData<List<CategoryWrapper>> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_category, parent, false);
        return new CategoryViewHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        Category category = getItem(index).getCategory();
        holder.setCategoryName(category.getName());
        holder.setCategoryColor(category.getColor());
        holder.setBudget(getItem(index).getCategory().getBudget());
        getItem(index).getBalance().observe(context, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long balance) {
                holder.setBalance(balance);
            }
        });
    }
}

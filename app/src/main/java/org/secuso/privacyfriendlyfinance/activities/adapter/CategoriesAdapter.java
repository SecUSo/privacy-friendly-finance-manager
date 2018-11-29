package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

public class CategoriesAdapter extends EntityListAdapter<Category, CategoryViewHolder> {
        public CategoriesAdapter(BaseActivity context, LiveData<List<Category>> data) {
        super(context, data);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_card_category, parent, false);
        return new CategoryViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int index) {
        super.onBindViewHolder(holder, index);
        Category category = getItem(index);
        holder.setCategory(category);
    }
}

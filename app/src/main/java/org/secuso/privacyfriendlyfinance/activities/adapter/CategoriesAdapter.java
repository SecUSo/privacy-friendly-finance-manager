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

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.BaseActivity;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

/**
 * Adapter for category lists.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
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

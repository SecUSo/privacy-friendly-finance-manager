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

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.secuso.privacyfriendlyfinance.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for lists of entities.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public abstract class EntityListAdapter<E extends IdProvider, H extends RecyclerView.ViewHolder> extends ListAdapter<E, H> {
    protected final BaseActivity context;
    private List<OnItemClickListener<E>> listeners = new ArrayList<>();

    public EntityListAdapter(BaseActivity context, LiveData<List<E>> data) {
        super(new DiffUtil.ItemCallback<E>() {
            @Override
            public boolean areItemsTheSame(E oldItem, E newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(E oldItem, E newItem) {
                // TODO: implement equals for entities and AccountWrapper
                return oldItem.equals(newItem);
            }
        });

        this.context = context;
        data.observe(context, new Observer<List<E>>() {
            @Override
            public void onChanged(@Nullable List<E> newItems) {
                submitList(newItems);
            }
        });
    }


    @Override
    public void onBindViewHolder(@NonNull H holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    E item = getItem(position);
                    for (OnItemClickListener<E> listener : listeners) {
                        listener.onItemClick(item);
                    }
                }
            }
        });
    }

    public void onItemClick(OnItemClickListener<E> listener) {
        listeners.add(listener);
    }
}

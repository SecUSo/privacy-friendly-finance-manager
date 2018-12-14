package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.secuso.privacyfriendlyfinance.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityListAdapter<E extends IdProvider, H extends RecyclerView.ViewHolder> extends ListAdapter<E, H> {
    protected final Context context;
    private List<OnItemClickListener<E>> listeners = new ArrayList<>();

    public EntityListAdapter(BaseActivity context, LiveData<List<E>> data) {
        super(new DiffUtil.ItemCallback<E>() {
            @Override
            public boolean areItemsTheSame(E oldItem, E newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(E oldItem, E newItem) {
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

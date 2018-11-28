package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.secuso.privacyfriendlyfinance.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    protected final Context context;
    protected List<T> items = new ArrayList<>();
    private List<OnItemClickListener<T>> listeners = new ArrayList<>();


    public ListAdapter(BaseActivity context, LiveData<List<T>> data) {
        this.context = context;
        data.observe(context, new Observer<List<T>>() {
            @Override
            public void onChanged(@Nullable List<T> newItems) {
                items = newItems;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("W4K","clicked" + position);
                itemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    void itemClicked(int position) {
        T item = items.get(position);
        for (OnItemClickListener<T> listener : listeners) {
            listener.onItemClick(item);
        }
    }

    public void onItemClick(OnItemClickListener<T> listener) {
        listeners.add(listener);
    }
}

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AbstractRecyclerViewHolder extends RecyclerView.ViewHolder {
    protected final Context context;

    public AbstractRecyclerViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
    }
}

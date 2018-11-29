package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewName;
    private TextView textViewAmount;
    private Category category;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.category_card_name);
        textViewAmount = itemView.findViewById(R.id.account_card_balance_current);
    }

    public void setCategory(Category category) {
        this.category = category;
        textViewName.setText(category.getName());
    }
    public Category getCategory() {
        return category;
    }
}

package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

public class CategoryViewHolder extends AbstractRecyclerViewHolder {
    private TextView textViewName;
    private TextView textViewBalance;
    private ImageView ivCategory;

    public CategoryViewHolder(@NonNull View itemView, Context context) {
        super(itemView, context);
        textViewName = itemView.findViewById(R.id.textView_category_name);
        textViewBalance = itemView.findViewById(R.id.textView_balance);
        ivCategory = itemView.findViewById(R.id.imageView_category_icon);
    }

    public void setCategoryName(String name) {
        textViewName.setText(name);
    }

    public void setBalance(Long balance) {
        CurrencyHelper.setBalance(balance, textViewBalance);
    }

    public void setCategoryColor(Integer color) {
        if (color != null) {
            ivCategory.setColorFilter(color);
        } else {
            ivCategory.clearColorFilter();
        }

    }
}

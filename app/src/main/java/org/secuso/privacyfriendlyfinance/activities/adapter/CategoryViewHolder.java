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
    private TextView textViewBudget;
    private TextView textViewBudgetLabel;
    private TextView textViewBudgetLeft;
    private ImageView ivCategory;
    private Long budget;

    public CategoryViewHolder(@NonNull View itemView, Context context) {
        super(itemView, context);
        textViewName = itemView.findViewById(R.id.textView_category_name);
        textViewBalance = itemView.findViewById(R.id.textView_balance);
        textViewBudget = itemView.findViewById(R.id.textView_budget);
        textViewBudgetLabel = itemView.findViewById(R.id.textView_budget_label);
        textViewBudgetLeft = itemView.findViewById(R.id.textView_budget_left);
        ivCategory = itemView.findViewById(R.id.imageView_category_icon);
    }

    public void setCategoryName(String name) {
        textViewName.setText(name);
    }

    public void setBalance(Long balance) {
        CurrencyHelper.setBalance(balance, textViewBalance);
        if (budget != null && balance != null) {
            CurrencyHelper.setBalance(budget + balance, textViewBudgetLeft);
            textViewBudgetLeft.setVisibility(View.VISIBLE);
        } else {
            textViewBudgetLeft.setVisibility(View.INVISIBLE);
        }
    }

    public void setBudget(Long budget) {
        this.budget = budget;
        if (budget == null) {
            textViewBudget.setVisibility(View.INVISIBLE);
            textViewBudgetLabel.setVisibility(View.INVISIBLE);
            textViewBudgetLeft.setVisibility(View.INVISIBLE);
        } else {
            textViewBudget.setVisibility(View.VISIBLE);
            textViewBudgetLabel.setVisibility(View.VISIBLE);

        }
        CurrencyHelper.setBalance(budget, textViewBudget, false);
    }

    public void setCategoryColor(Integer color) {
        if (color != null) {
            ivCategory.setColorFilter(color);
        } else {
            ivCategory.clearColorFilter();
        }
    }
}

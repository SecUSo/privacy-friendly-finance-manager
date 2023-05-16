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

package org.secuso.privacyfriendlyfinance.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.dialog.CategoryDialog;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoryViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

/**
 * Activity that shows detailed information about a single category and all transactions that are
 * linked to this category.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class CategoryActivity extends TransactionListActivity {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";
    protected CategoryViewModel viewModel;
    private TextView tvCategoryBudgetLabel;
    private TextView tvCategoryBudget;
    private TextView tvCategoryBudgetMonth;
    private Long budget = null;
    private Long balance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (CategoryViewModel) super.viewModel;
        viewModel.getCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                viewModel.setTitle(category.getName());
            }
        });
    }

    private void updateBudgetMonth() {
        if (budget == null) {
            tvCategoryBudgetLabel.setVisibility(View.INVISIBLE);
            tvCategoryBudget.setVisibility(View.INVISIBLE);
            tvCategoryBudgetMonth.setVisibility(View.INVISIBLE);
        } else {
            if (balance == null) balance = 0L;

            CurrencyHelper.setBalance(budget + balance, tvCategoryBudgetMonth);
            tvCategoryBudget.setText("/ " + CurrencyHelper.convertToCurrencyString(budget));
            tvCategoryBudgetLabel.setVisibility(View.VISIBLE);
            tvCategoryBudget.setVisibility(View.VISIBLE);
            tvCategoryBudgetMonth.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addEditMenuClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openCategoryDialog();

                return true;
            }
        });

        View view = setHeaderLayout(R.layout.header_category_balance);

        tvCategoryBudgetLabel = view.findViewById(R.id.tv_categoryBudgetMonth_label);
        tvCategoryBudget = view.findViewById(R.id.tv_categoryBudget);
        tvCategoryBudgetMonth = view.findViewById(R.id.tv_categoryBudgetMonth);

        final TextView tvCategoryBalance = view.findViewById(R.id.tv_categoryBalanceMonth);
        final TextView tvCategoryIncome = view.findViewById(R.id.tv_categoryIncomeMonth);
        final TextView tvCategoryExpenses = view.findViewById(R.id.tv_categoryExpensesMonth);

        viewModel.getCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(@Nullable Category category) {
                budget = category.getBudget();
                updateBudgetMonth();
            }
        });

        viewModel.getCategoryBalanceMonth().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long currencyBalance) {
                CurrencyHelper.setBalance(currencyBalance, tvCategoryBalance);
                balance = currencyBalance;
                updateBudgetMonth();

            }
        });
        viewModel.getCategoryIncomeMonth().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long categoryIncome) {
                CurrencyHelper.setBalance(categoryIncome, tvCategoryIncome);
            }
        });
        viewModel.getCategoryExpensesMonth().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long categoryExpenses) {
                CurrencyHelper.setBalance(categoryExpenses, tvCategoryExpenses);
            }
        });
    }

    private void openCategoryDialog() {
        CategoryDialog.showCategoryDialog(viewModel.getCategory().getValue(), getSupportFragmentManager());
    }

    @Override
    protected CategoryViewModel getViewModel() {
        long categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1);
        CategoryViewModel.CategoryViewModelFactory viewModelFactory = new CategoryViewModel.CategoryViewModelFactory(this.getApplication(), categoryId);
        return new ViewModelProvider(this, viewModelFactory).get(CategoryViewModel.class);
    }

    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return CategoryViewModel.class;
    }
}

package org.secuso.privacyfriendlyfinance.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoryViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

public class CategoryActivity extends TransactionListActivity {
    public static final String EXTRA_CATEGORY_ID = "org.secuso.privacyfriendlyfinance.EXTRA_CATEGORY_ID";
    protected CategoryViewModel viewModel;

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

        final TextView tvCategoryBalance = view.findViewById(R.id.tv_categoryBalance);
        final TextView tvCategoryIncome = view.findViewById(R.id.tv_categoryIncome);
        final TextView tvCategoryExpenses = view.findViewById(R.id.tv_categoryExpenses);

        viewModel.getCategoryBalance().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long currencyBalance) {
                CurrencyHelper.setBalance(currencyBalance, tvCategoryBalance);
            }
        });
        viewModel.getCategoryIncome().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long categoryIncome) {
                CurrencyHelper.setBalance(categoryIncome, tvCategoryIncome);
            }
        });
        viewModel.getCategoryExpenses().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long categoryExpenses) {
                CurrencyHelper.setBalance(categoryExpenses, tvCategoryExpenses);
            }
        });
    }

    private void openCategoryDialog() {
        CategoryDialog categoryDialog = new CategoryDialog();

        Bundle args = new Bundle();
        args.putLong(CategoryDialog.EXTRA_CATEGORY_ID, viewModel.getCategory().getValue().getId());
        categoryDialog.setArguments(args);

        categoryDialog.show(getSupportFragmentManager(), "CategoryDialog");
    }

    @Override
    protected CategoryViewModel getViewModel() {
        long categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1);
        CategoryViewModel.CategoryViewModelFactory viewModelFactory = new CategoryViewModel.CategoryViewModelFactory(this.getApplication(), categoryId);
        return ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
    }

    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return CategoryViewModel.class;
    }
}

package org.secuso.privacyfriendlyfinance.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import org.secuso.privacyfriendlyfinance.activities.viewmodel.CategoryViewModel;
import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

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

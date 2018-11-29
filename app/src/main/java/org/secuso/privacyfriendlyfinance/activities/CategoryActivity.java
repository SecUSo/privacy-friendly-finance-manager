package org.secuso.privacyfriendlyfinance.activities;

import org.secuso.privacyfriendlyfinance.activities.viewmodel.TransactionListViewModel;

public class CategoryActivity extends TransactionListActivity {
    @Override
    protected Class<? extends TransactionListViewModel> getViewModelClass() {
        return TransactionListViewModel.class;
    }
}

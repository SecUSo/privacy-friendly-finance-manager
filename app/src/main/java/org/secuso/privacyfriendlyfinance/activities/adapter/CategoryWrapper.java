package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

public class CategoryWrapper implements IdProvider {
    private Category category;
    private LiveData<Long> balance;

    public CategoryWrapper(Category category) {
        this.category = category;
        balance = FinanceDatabase.getInstance().transactionDao().sumForCategory(category.getId());
    }

    public LiveData<Long> getBalance() {
        return balance;
    }

    @Override
    public Long getId() {
        return category.getId();
    }

    public Category getCategory() {
        return category;
    }
}

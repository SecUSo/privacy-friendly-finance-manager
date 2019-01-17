package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

public class CategoryWrapper implements IdProvider {
    private Category category;
    private LiveData<Long> balance;

    public CategoryWrapper(Category category) {
        this.category = category;
        balance = FinanceDatabase.getInstance().transactionDao().sumForCategoryFrom(category.getId(), LocalDate.now().withDayOfMonth(1).toString());
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

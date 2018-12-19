package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

public class CategoryViewModel extends TransactionListViewModel {
    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
    private long categoryId;
    private LiveData<Category> category;
    private LiveData<Long> categoryBalance;
    private LiveData<Long> categoryIncome;
    private LiveData<Long> categoryExpenses;
    private LiveData<List<Transaction>> transactions;

    public CategoryViewModel(@NonNull Application application, long categoryId) {
        super(application);
        setNavigationDrawerId(R.id.nav_category);
        this.categoryId = categoryId;
        category = categoryDao.get(categoryId);
        categoryBalance = FinanceDatabase.getInstance().transactionDao().sumForCategory(categoryId);
        categoryIncome = FinanceDatabase.getInstance().transactionDao().sumIncomeForCategory(categoryId);
        categoryExpenses = FinanceDatabase.getInstance().transactionDao().sumExpensesForCategory(categoryId);
        setNavigationDrawerId(R.id.nav_category);
        setPreselectedCategoryId(categoryId);
        setShowEditMenu(true);
    }

    public LiveData<Long> getCategoryIncome() {
        return categoryIncome;
    }

    public LiveData<Long> getCategoryExpenses() {
        return categoryExpenses;
    }

    public LiveData<Long> getCategoryBalance() {
        return categoryBalance;
    }

    public LiveData<Category> getCategory() {
        return category;
    }

    @Override
    protected LiveData<List<Transaction>> fetchTransactions() {
        return transactionDao.getForCategory(categoryId);
    }

    public static class CategoryViewModelFactory implements ViewModelProvider.Factory {
        private Application application;
        private long categoryId;

        public CategoryViewModelFactory(Application application, long categoryId) {
            this.application = application;
            this.categoryId = categoryId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CategoryViewModel(application, categoryId);
        }
    }
}


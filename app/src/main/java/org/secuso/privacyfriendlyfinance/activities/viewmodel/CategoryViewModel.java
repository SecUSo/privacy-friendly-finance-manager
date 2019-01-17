package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
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
    private LiveData<Long> categoryBalanceMonth;
    private LiveData<Long> categoryIncomeMonth;
    private LiveData<Long> categoryExpensesMonth;
    private LiveData<List<Transaction>> transactions;

    public CategoryViewModel(@NonNull Application application, long categoryId) {
        super(application);
        setNavigationDrawerId(R.id.nav_category);
        this.categoryId = categoryId;
        category = categoryDao.get(categoryId);
        categoryBalanceMonth = FinanceDatabase.getInstance().transactionDao().sumForCategoryFrom(categoryId, LocalDate.now().withDayOfMonth(1).toString());
        categoryIncomeMonth = FinanceDatabase.getInstance().transactionDao().sumIncomeForCategoryFrom(categoryId, LocalDate.now().withDayOfMonth(1).toString());
        categoryExpensesMonth = FinanceDatabase.getInstance().transactionDao().sumExpensesForCategoryFrom(categoryId, LocalDate.now().withDayOfMonth(1).toString());
        setNavigationDrawerId(R.id.nav_category);
        setPreselectedCategoryId(categoryId);
        setShowEditMenu(true);
    }

    public LiveData<Long> getCategoryIncomeMonth() {
        return categoryIncomeMonth;
    }

    public LiveData<Long> getCategoryExpensesMonth() {
        return categoryExpensesMonth;
    }

    public LiveData<Long> getCategoryBalanceMonth() {
        return categoryBalanceMonth;
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


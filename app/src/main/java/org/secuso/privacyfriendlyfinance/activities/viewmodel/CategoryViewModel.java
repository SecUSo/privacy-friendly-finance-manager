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

package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

/**
 * View model for the category activity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class CategoryViewModel extends TransactionListViewModel {
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
        category = FinanceDatabase.getInstance(application).categoryDao().get(categoryId);
        categoryBalanceMonth = FinanceDatabase.getInstance(application).transactionDao().sumForCategoryThisMonth(categoryId);
        categoryIncomeMonth = FinanceDatabase.getInstance(application).transactionDao().sumIncomeForCategoryThisMonth(categoryId);
        categoryExpensesMonth = FinanceDatabase.getInstance(application).transactionDao().sumExpensesForCategoryThisMonth(categoryId);
        setNavigationDrawerId(R.id.nav_category);
        setPreselectedCategoryId(categoryId);
        setShowEditMenu(true);
    }

    @Override
    public boolean showDrawer() {
        return false;
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

    @Override
    protected LiveData<List<Transaction>> fetchTransactionsFiltered(String filter) {
        return transactionDao.getForCategoryFiltered(categoryId, filter);
    }

    public static class CategoryViewModelFactory implements ViewModelProvider.Factory {
        private Application application;
        private long categoryId;

        public CategoryViewModelFactory(Application application, long categoryId) {
            this.application = application;
            this.categoryId = categoryId;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new CategoryViewModel(application, categoryId);
        }
    }
}


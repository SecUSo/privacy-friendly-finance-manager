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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.activities.adapter.IdProvider;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for the transaction dialog.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class TransactionDialogViewModel extends CurrencyInputBindableViewModel {
    private final CategoryDao categoryDao = FinanceDatabase.getInstance(getApplication()).categoryDao();
    private final AccountDao accountDao = FinanceDatabase.getInstance(getApplication()).accountDao();
    private final TransactionDao transactionDao = FinanceDatabase.getInstance(getApplication()).transactionDao();

    private final LiveData<List<Account>> accounts;
    private final LiveData<List<Category>> categories;

    private LiveData<Transaction> transactionLive;
    private LiveData<RepeatingTransaction> repeatingTransaction;
    private Transaction transaction;

    private final Application application;

    private boolean amountEdited = false;


    private long transactionId = -1;

    public TransactionDialogViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        categories = Transformations.map(categoryDao.getAll(), input -> {
            List<Category> categoriesAndVoid = new ArrayList<>();
            categoriesAndVoid.add(null);
            categoriesAndVoid.addAll(input);
            return categoriesAndVoid;
        });

        accounts = accountDao.getAll();
        Transformations.map(accounts, (Function<List<Account>, Void>) input -> {
            notifyPropertyChanged(BR.accountIndex);
            return null;
        });

        setTransactionDummy();
    }

    public LiveData<List<String>> getAllDistinctTitles() {
        return transactionDao.getAllDistinctTitles();
    }

    @Override
    protected Long getNumericAmount() {
        if (transaction == null) return null;
        if (transaction.getId() == null && !amountEdited) return null;
        return transaction.getAmount();
    }

    @Override
    protected void setNumericAmount(Long amount) {
        amountEdited = true;
        if (amount == null) amount = 0L;
        transaction.setAmount(amount);
    }

    public LiveData<List<Category>> getAllCategories() {
        return categories;
    }
    public LiveData<List<Account>> getAllAccounts() {
        return accounts;
    }

    public LiveData<Transaction> setTransactionId(long transactionId) {
        if (this.transactionId != transactionId) {
            this.transactionId = transactionId;
            if (transactionId == -1) {
                setTransactionDummy();
            } else {
                transactionLive = transactionDao.get(transactionId);
            }
        }
        return transactionLive;
    }

    private void setTransactionDummy() {
        MutableLiveData<Transaction> mutableTransaction = new MutableLiveData<>();
        mutableTransaction.postValue(new Transaction());
        transactionLive = mutableTransaction;
    }


    private String originalName;
    private Long originalAccountId;
    private Long originalCategoryId;
    private Long originalAmount;
    private LocalDate originalDate;
    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        originalName = transaction.getName();
        originalAccountId = transaction.getAccountId();
        originalCategoryId = transaction.getCategoryId();
        originalAmount = transaction.getAmount();
        originalDate = transaction.getDate();

        if (transaction.getRepeatingId() != null) {
            repeatingTransaction = FinanceDatabase.getInstance(application)
                    .repeatingTransactionDao().get(transaction.getRepeatingId());
        }
        notifyChange();
    }

    public LiveData<RepeatingTransaction> getRepeatingTransaction() {
        return repeatingTransaction;
    }

    @Bindable
    public String getName() {
        return transaction.getName();
    }
    public void setName(String name) {
        if (name == null) name = "";
        if (transaction.getName() == null) transaction.setName("");
        if (!transaction.getName().equals(name)) {
            transaction.setName(name);
            notifyPropertyChanged(BR.name);
        }
    }

    @Bindable
    public String getDateString() {
        return transaction.getDate().toString();
    }
    public LocalDate getDate() {
        return transaction.getDate();
    }
    public void setDate(LocalDate date) {
        if (date != null && !transaction.getDate().equals(date)){
            transaction.setDate(date);
            notifyPropertyChanged(BR.dateString);
        }
    }

    private int indexOfId(List<? extends IdProvider> list, Long id) {
        if (list == null) return 0;
        for (int i = 0; i < list.size(); ++i) {
            IdProvider element = list.get(i);
            if ((element != null && element.getId() == id) || (element == null && id == null)) {
                return i;
            }
        }
        return 0;
    }

    @Bindable
    public int getAccountIndex() {
        if (accounts.getValue() == null || transaction == null) return 0;
        return indexOfId(accounts.getValue(), transaction.getAccountId());
    }
    public void setAccountIndex(int accountIndex) {
        Log.d("accountIndex", "" + accountIndex);
        Account account = accounts.getValue().get(accountIndex);
        if (transaction.getAccountId() != account.getId()) {
            transaction.setAccountId(account.getId());
            notifyPropertyChanged(BR.accountIndex);
        }
    }

    @Bindable
    public int getCategoryIndex() {
        if (categories.getValue() == null|| transaction == null) return 0;
        return indexOfId(categories.getValue(), transaction.getCategoryId());
    }
    public void setCategoryIndex(int categoryIndex) {
        Log.d("categoryIndex", "" + categoryIndex);
        Category category = categories.getValue().get(categoryIndex);
        if (category == null) {
            if (transaction.getCategoryId() != null) {
                transaction.setCategoryId(null);
                notifyPropertyChanged(BR.categoryIndex);
            }
        } else if (transaction.getCategoryId() != category.getId()) {
            transaction.setCategoryId(category.getId());
            notifyPropertyChanged(BR.categoryIndex);
        }
    }


    public void submit() {
        if (transaction.getName() != null) {
            transaction.setName(transaction.getName().trim());
        }
        transactionDao.updateOrInsertAsync(transaction);
    }

    public void cancel() {
        transaction.setName(originalName);
        transaction.setAccountId(originalAccountId);
        transaction.setCategoryId(originalCategoryId);
        transaction.setAmount(originalAmount);
        transaction.setDate(originalDate);
    }
}

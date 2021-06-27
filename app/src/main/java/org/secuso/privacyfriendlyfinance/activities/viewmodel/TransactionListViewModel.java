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

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

/**
 * View model for all classes that show lists of transactions.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class TransactionListViewModel extends BaseViewModel {
    protected final TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
    private LiveData<Long> balance;
    private LiveData<List<Transaction>> transactions;
    private long preselectedCategoryId = -1L;
    private long preselectedAccountId = -1L;


    public TransactionListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Long> getBalance() {
        return balance;
    }

    public void setBalance(LiveData<Long> balance) {
        this.balance = balance;
    }

    protected LiveData<List<Transaction>> fetchTransactions() {
        return transactionDao.getAll();
    }

    public LiveData<List<Transaction>> getTransactions() {
        if (transactions == null) transactions = fetchTransactions();
        return transactions;
    }

    public long getPreselectedCategoryId() {
        return preselectedCategoryId;
    }

    public void setPreselectedCategoryId(long preselectedCategoryId) {
        this.preselectedCategoryId = preselectedCategoryId;
    }

    public long getPreselectedAccountId() {
        return preselectedAccountId;
    }

    public void setPreselectedAccountId(long preselectedAccountId) {
        this.preselectedAccountId = preselectedAccountId;
    }

}

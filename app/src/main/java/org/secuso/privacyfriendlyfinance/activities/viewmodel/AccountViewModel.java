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

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

/**
 * View model for the account activity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class AccountViewModel extends TransactionListViewModel {
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private long accountId;
    private LiveData<Account> account;
    private LiveData<Long> totalBalance;
    private LiveData<Long> monthBalance;

    public AccountViewModel(@NonNull Application application, long accountId) {
        super(application);
        this.accountId = accountId;
        account = accountDao.get(accountId);
        totalBalance = FinanceDatabase.getInstance().transactionDao().sumForAccount(accountId);
        monthBalance = FinanceDatabase.getInstance().transactionDao().sumForAccountBefore(accountId, LocalDate.now().withDayOfMonth(1).toString());
        setNavigationDrawerId(null);
        setPreselectedAccountId(accountId);
        setShowEditMenu(true);
    }

    @Override
    public boolean showDrawer() {
        return false;
    }

    public LiveData<Long> getTotalBalance() {
        return totalBalance;
    }
    public LiveData<Long> getMonthBalance() {
        return monthBalance;
    }

    public LiveData<Account> getAccount() {
        return account;
    }

    @Override
    protected LiveData<List<Transaction>> fetchTransactions() {
        return transactionDao.getForAccount(accountId);
    }

    public static class AccountViewModelFactory implements ViewModelProvider.Factory {
        private Application application;
        private long accountId;

        public AccountViewModelFactory(Application application, long accountId) {
            this.application = application;
            this.accountId = accountId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new AccountViewModel(application, accountId);
        }
    }
}

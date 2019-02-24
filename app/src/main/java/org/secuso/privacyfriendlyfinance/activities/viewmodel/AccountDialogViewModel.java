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
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

/**
 * View model for the account dialog.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class AccountDialogViewModel extends CurrencyInputBindableViewModel {
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
    private Account account;
    private LiveData<Long> monthBalanceLive;
    private LiveData<Account> accountLive;
    private Long accountId;
    private long initialMonthBalance = 0;
    private Long monthBalance;
    private String originalName;

    public AccountDialogViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Long getNumericAmount() {
        return monthBalance;
    }

    @Override
    protected void setNumericAmount(Long amount) {
        monthBalance = amount;
    }

    public LiveData<List<Account>> getAllAccounts() {
        return accountDao.getAll();
    }

    public void updateOrInsert(Account account) {
        accountDao.updateOrInsertAsync(account);
    }

    public void setAccount(Account account) {
        this.account = account;
        originalName = account.getName();
        notifyChange();
    }

    public LiveData<Account> setAccountId(long accountId) {
        if (this.accountId == null || this.accountId != accountId) {
            this.accountId = accountId;
            if (accountId == -1) {
                setAccountDummy();
            } else {
                accountLive = accountDao.get(accountId);
            }
        }
        return accountLive;
    }

    private void setAccountDummy() {
        MutableLiveData<Account> mutable = new MutableLiveData<>();
        mutable.postValue(new Account());
        accountLive = mutable;
    }

    public void setInitialMonthBalance(long initialMonthBalance) {
        this.initialMonthBalance = initialMonthBalance;
        if (monthBalance == null) monthBalance = initialMonthBalance;
    }

    @Bindable
    public String getName() {
        return account.getName();
    }
    public void setName(String name) {
        if (name == null) name = "";
        if (account.getName() == null) account.setName("");
        if (!account.getName().equals(name)) {
            account.setName(name);
            notifyPropertyChanged(BR.name);
        }
    }

    public void submit(final String compensationTitle) {
        accountDao.updateOrInsertAsync(account).addListener(new TaskListener() {
            @Override
            public void onDone(Object result, AsyncTask<?, ?, ?> task) {
                if (initialMonthBalance != monthBalance) {
                    Long id = (Long) result;
                    Transaction compensation = new Transaction();
                    compensation.setName(compensationTitle);
                    compensation.setAccountId(id);
                    compensation.setDate(LocalDate.now().withDayOfMonth(1).minusDays(1));
                    compensation.setAmount(monthBalance - initialMonthBalance);
                    transactionDao.updateOrInsertAsync(compensation);
                }
            }
        });
    }

    public void cancel() {
        account.setName(originalName);
    }

    public Account getAccount() {
        return account;
    }
}

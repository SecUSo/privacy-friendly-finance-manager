package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountWrapper implements IdProvider {
    private Account account;
    private LiveData<Long> currentBalance;
    private LiveData<Long> startOfMonthBalance;

    public AccountWrapper(Account account) {
        this.account = account;
        currentBalance = FinanceDatabase.getInstance().transactionDao().sumForAccount(account.getId());
        startOfMonthBalance = FinanceDatabase.getInstance().transactionDao().sumForAccountBefore(account.getId(), LocalDate.now().withDayOfMonth(1).toString());
    }

    public LiveData<Long> getCurrentBalance() {
        return currentBalance;
    }
    public LiveData<Long> getStartOfMonthBalance() {
        return startOfMonthBalance;
    }

    @Override
    public Long getId() {
        return account.getId();
    }

    public Account getAccount() {
        return account;
    }

}

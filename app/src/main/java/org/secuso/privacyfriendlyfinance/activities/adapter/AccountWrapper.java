package org.secuso.privacyfriendlyfinance.activities.adapter;

import android.arch.lifecycle.LiveData;

import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountWrapper {
    private Account account;
    private LiveData<Long> currentBalance;
    private LiveData<Long> startOfMonthBalance;

    public LiveData<Long> getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(LiveData<Long> currentBalance) {
        this.currentBalance = currentBalance;
    }

    public LiveData<Long> getStartOfMonthBalance() {
        return startOfMonthBalance;
    }

    public void setStartOfMonthBalance(LiveData<Long> startOfMonthBalance) {
        this.startOfMonthBalance = startOfMonthBalance;
    }
}

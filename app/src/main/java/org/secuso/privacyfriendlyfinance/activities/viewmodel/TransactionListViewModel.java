package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

public class TransactionListViewModel extends BaseViewModel {
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
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

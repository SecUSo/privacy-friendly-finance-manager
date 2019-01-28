package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

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

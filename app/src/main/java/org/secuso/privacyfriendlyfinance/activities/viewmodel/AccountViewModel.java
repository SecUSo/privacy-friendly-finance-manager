package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountViewModel extends TransactionListViewModel {
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private long accountId;
    private LiveData<Account> account;

    public AccountViewModel(@NonNull Application application, long accountId) {
        super(application);
        this.accountId = accountId;
        account = accountDao.get(accountId);
        setNavigationDrawerId(R.id.nav_account);
        setPreselectedAccountId(accountId);
    }

    public LiveData<Account> getAccount() {
        return account;
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

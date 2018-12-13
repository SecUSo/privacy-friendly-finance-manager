package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

public class AccountDialogViewModel extends BaseViewModel {
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();

    public AccountDialogViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Account> getAccountById(long accountId) {
        return accountDao.get(accountId);
    }

    public void updateOrInsert(Account account) {
        accountDao.updateOrInsertAsync(account);
    }
}

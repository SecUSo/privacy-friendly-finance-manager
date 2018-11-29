package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

public class AccountsViewModel extends BaseViewModel {
    private AccountDao dao = FinanceDatabase.getInstance().accountDao();
    private LiveData<List<Account>> accounts;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        accounts = dao.getAll();
        setNavigationDrawerId(R.id.nav_account);
    }

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }
}


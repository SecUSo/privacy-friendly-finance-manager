package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.convert.LocalDateConverter;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

public class AccountsViewModel extends BaseViewModel {
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();

    private LiveData<List<Account>> accounts;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        accounts = accountDao.getAll();
        setNavigationDrawerId(R.id.nav_account);
    }

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }

    public LiveData<Long> getCurrentBalanceForAccount(long accountId) {
        return transactionDao.sumForAccount(accountId);
    }

    public LiveData<Long> getStartOfMonthBalanceForAccount(long accountId) {
        LocalDate ld = new LocalDate(0L).plusYears(LocalDate.now().getYear()).plusMonths(LocalDate.now().getMonthOfYear());
        return transactionDao.sumForAccountBefore(accountId, LocalDateConverter.dateToString(ld));
    }
}


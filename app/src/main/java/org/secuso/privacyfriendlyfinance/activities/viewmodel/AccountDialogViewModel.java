package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
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

public class AccountDialogViewModel extends CurrencyInputBindableViewModel {
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();
    private Account account;
    private LiveData<Long> monthBalanceLive;
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

    public void setAccountId(long accountId) {
        account = accountId == -1 ? new Account() : accountDao.getCached(accountId);
        if (account != null) originalName = account.getName();
    }

    public boolean isNewAccount() {
        return account.getId() == null;
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
}

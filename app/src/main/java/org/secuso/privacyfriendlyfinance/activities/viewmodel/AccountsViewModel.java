package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.AccountWrapper;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountsViewModel extends BaseViewModel {
    private LiveData<List<AccountWrapper>> accounts;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        setNavigationDrawerId(R.id.nav_account);
        setTitle(R.string.activity_accounts_title);
        accounts = Transformations.map(FinanceDatabase.getInstance().accountDao().getAll(), new Function<List<Account>, List<AccountWrapper>>() {
            @Override
            public List<AccountWrapper> apply(List<Account> input) {
                List<AccountWrapper> wrappers = new ArrayList<>();
                for (Account account : input) {
                    wrappers.add(new AccountWrapper(account));
                }
                return wrappers;
            }
        });
    }

    public LiveData<List<AccountWrapper>> getAccounts() {
        return accounts;
    }
}


package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

public class RepeatingTransactionsViewModel extends BaseViewModel {
    private LiveData<List<RepeatingTransaction>> repeatingTransactions;

    public RepeatingTransactionsViewModel(@NonNull Application application) {
        super(application);
        setNavigationDrawerId(R.id.nav_repeating_transactions);
        setTitle(R.string.activity_repeating_transactions_title);
        repeatingTransactions = FinanceDatabase.getInstance()
                .repeatingTransactionDao().getAll();
    }

    public LiveData<List<RepeatingTransaction>> getRepeatingTransactions() {
        return repeatingTransactions;
    }
}

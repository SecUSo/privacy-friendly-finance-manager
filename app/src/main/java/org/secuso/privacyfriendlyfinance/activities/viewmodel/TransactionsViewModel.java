package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;

public class TransactionsViewModel extends TransactionListViewModel {
    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        setTitle(R.string.activity_transactions_title);
        setNavigationDrawerId(R.id.nav_main);
    }
}

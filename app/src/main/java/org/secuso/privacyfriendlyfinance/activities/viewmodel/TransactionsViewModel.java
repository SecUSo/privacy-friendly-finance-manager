package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;

public class TransactionsViewModel extends TransactionListViewModel {
    public TransactionsViewModel(@NonNull Application application) {
        super(application);
//        TODO
//        setTitle(R.string.activity_transaction_latest_title);
        setNavigationDrawerId(R.id.nav_main);
    }
}

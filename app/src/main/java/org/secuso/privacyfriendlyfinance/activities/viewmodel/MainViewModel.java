package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;

public class MainViewModel extends TransactionListViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
        setTitle("MAin!");
        setNavigationDrawerId(R.id.nav_main);
    }
}

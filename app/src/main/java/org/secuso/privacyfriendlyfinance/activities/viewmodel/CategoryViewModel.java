package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;

public class CategoryViewModel extends TransactionListViewModel {
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        setNavigationDrawerId(R.id.nav_category);
    }
}

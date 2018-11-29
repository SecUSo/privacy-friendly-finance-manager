package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

public class TransactionListViewModel extends AndroidViewModel {
    private String title;
    private LiveData<Long> balance;
    private LiveData<List<Transaction>> transactions;


    public TransactionListViewModel(@NonNull Application application) {
        super(application);
    }
}

package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

public class TransactionDialogViewModel extends AndroidViewModel {
    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();

    private LiveData<List<Category>> categories = categoryDao.getAll();
    private LiveData<List<Account>> accounts = accountDao.getAll();
    private LiveData<Transaction> transaction;
    private MutableLiveData<Long> amount = new MutableLiveData<>();


    private long transactionId = -1;

    public TransactionDialogViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Category>> getAllCategories() {
        return categories;
    }

    public LiveData<List<Account>> getAllAccounts() {
        return accounts;
    }

    public void editOrInsertTransaction(Transaction transaction) {
        transactionDao.updateOrInsertAsync(transaction);
    }

    public void setTransactionId(long transactionId) {
        if (this.transactionId != transactionId) {
            this.transactionId = transactionId;
            if (transactionId == -1) {
                MutableLiveData<Transaction> mutableTransaction = new MutableLiveData<>();
                mutableTransaction.postValue(new Transaction());
                transaction = mutableTransaction;
            } else {
                transaction = transactionDao.get(transactionId);
            }
        }
    }

    public LiveData<Transaction> getTransaction() {
        return transaction;
    }
}

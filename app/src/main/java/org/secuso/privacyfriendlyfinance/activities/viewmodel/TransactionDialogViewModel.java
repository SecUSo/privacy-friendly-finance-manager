package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.IdProvider;
import org.secuso.privacyfriendlyfinance.activities.helper.CurrencyTextWatcher;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDialogViewModel extends AndroidViewModel implements Observable {
    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();

//    private List<Category> categories;
    private LiveData<List<Category>> categories;
    private LiveData<List<Account>> accounts = accountDao.getAll();

    private LiveData<Transaction> transactionLive;
    private Transaction transaction;


    private long transactionId = -1;

    public TransactionDialogViewModel(@NonNull Application application) {
        super(application);
        categories = Transformations.map(categoryDao.getAll(), new Function<List<Category>, List<Category>>() {
            @Override
            public List<Category> apply(List<Category> input) {
                List<Category> categoriesAndVoid = new ArrayList<>();
                categoriesAndVoid.add(null);
                categoriesAndVoid.addAll(input);
                return categoriesAndVoid;
            }
        });

        Transformations.map(accounts, new Function<List<Account>, Void>() {
            @Override
            public Void apply(List<Account> input) {
                notifyPropertyChanged(BR.accountIndex);
                return null;
            }
        });

        setTransactionDummy();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categories;
    }
    public LiveData<List<Account>> getAllAccounts() {
        return accounts;
    }

    public LiveData<Transaction> setTransactionId(long transactionId) {
        if (this.transactionId != transactionId) {
            this.transactionId = transactionId;
            if (transactionId == -1) {
                setTransactionDummy();
            } else {
                transactionLive = transactionDao.get(transactionId);
            }
        }
        return transactionLive;
    }

    private void setTransactionDummy() {
        MutableLiveData<Transaction> mutableTransaction = new MutableLiveData<>();
        mutableTransaction.postValue(new Transaction());
        transactionLive = mutableTransaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        notifyChange();
    }


    @Bindable
    public String getName() {
        return transaction.getName();
    }
    public void setName(String name) {
        if (name == null) name = "";
        if (transaction.getName() == null) transaction.setName("");
        if (!transaction.getName().equals(name)) {
            transaction.setName(name);
            notifyPropertyChanged(BR.name);
        }
    }

    @Bindable
    public String getAmount() {
        return CurrencyTextWatcher.convertToString(transaction.getAmount());
    }
    public void setAmount(String amount) {
        if (amount == null) amount = "";
        Long number = CurrencyTextWatcher.convertToLong(amount);
        if (number == null) number = 0L;
        if (transaction.getAmount() != number){
            transaction.setAmount(number);
            notifyPropertyChanged(BR.amount);
        }
    }

    @Bindable
    public int getAmountColor() {
        return getExpense() ? R.color.red : R.color.green;
    }

    @Bindable
    public boolean getExpense() {
        return transaction.getAmount() <= 0;
    }
    public void setExpense(boolean checked) {
        if ((transaction.getAmount() > 0 && checked) || (transaction.getAmount() < 0 && !checked)) {
            transaction.setAmount(transaction.getAmount() * -1);
            Log.d("amount", "" + transaction.getAmount());
            notifyPropertyChanged(BR.expense);
            notifyPropertyChanged(BR.amountColor);
        }
    }

    @Bindable
    public String getDateString() {
        return transaction.getDate().toString();
    }
    public LocalDate getDate() {
        return transaction.getDate();
    }
    public void setDate(LocalDate date) {
        if (date != null && !transaction.getDate().equals(date)){
            transaction.setDate(date);
            notifyPropertyChanged(BR.dateString);
        }
    }

    private int indexOfId(List<? extends IdProvider> list, Long id) {
        if (list == null) return 0;
        for (int i = 0; i < list.size(); ++i) {
            IdProvider element = list.get(i);
            if ((element != null && element.getId() == id) || (element == null && id == null)) {
                return i;
            }
        }
        return 0;
    }

    @Bindable
    public int getAccountIndex() {
        if (accounts.getValue() == null) return 0;
        return indexOfId(accounts.getValue(), transaction.getAccountId());
    }
    public void setAccountIndex(int accountIndex) {
        Log.d("accountIndex", "" + accountIndex);
        Account account = accounts.getValue().get(accountIndex);
        if (transaction.getAccountId() != account.getId()) {
            transaction.setAccountId(account.getId());
            notifyPropertyChanged(BR.accountIndex);
        }
    }

    @Bindable
    public int getCategoryIndex() {
        if (categories.getValue() == null) return 0;
        return indexOfId(categories.getValue(), transaction.getCategoryId());
    }
    public void setCategoryIndex(int categoryIndex) {
        Log.d("categoryIndex", "" + categoryIndex);
        Category category = categories.getValue().get(categoryIndex);
        if (category == null) {
            if (transaction.getCategoryId() != null) {
                transaction.setCategoryId(null);
                notifyPropertyChanged(BR.categoryIndex);
            }
        } else if (transaction.getCategoryId() != category.getId()) {
            transaction.setCategoryId(category.getId());
            notifyPropertyChanged(BR.categoryIndex);
        }
    }


    public void submit() {
        transactionDao.updateOrInsertAsync(transaction);
    }

    // The following is just the BaseObservalbe implementation.
    private transient PropertyChangeRegistry mCallbacks;
    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (mCallbacks == null) {
            mCallbacks = new PropertyChangeRegistry();
        }
        mCallbacks.add(callback);
        Log.w("transDilg", "callback added");
    }
    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public synchronized void notifyChange() {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, 0, null);
        }
    }
    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with {@link Bindable} to generate a field in
     * <code>BR</code> to be used as <code>fieldId</code>.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }


}

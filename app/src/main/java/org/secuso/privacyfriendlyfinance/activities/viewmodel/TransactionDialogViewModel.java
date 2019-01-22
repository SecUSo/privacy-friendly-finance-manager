package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.activities.adapter.IdProvider;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.AccountDao;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDialogViewModel extends CurrencyInputBindableViewModel {
    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
    private AccountDao accountDao = FinanceDatabase.getInstance().accountDao();
    private TransactionDao transactionDao = FinanceDatabase.getInstance().transactionDao();

    private LiveData<List<Category>> categories;
    private LiveData<List<Account>> accounts = accountDao.getAll();

    private LiveData<Transaction> transactionLive;
    private Transaction transaction;

    private Application application;


    private long transactionId = -1;

    public TransactionDialogViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
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

    @Override
    protected Long getNumericAmount() {
        return transaction.getAmount();
    }

    @Override
    protected void setNumericAmount(Long amount) {
        transaction.setAmount(amount);
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


    private String originalName;
    private Long originalAccountId;
    private Long originalCategoryId;
    private Long originalAmount;
    private LocalDate originalDate;
    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        originalName = transaction.getName();
        originalAccountId = transaction.getAccountId();
        originalCategoryId = transaction.getCategoryId();
        originalAmount = transaction.getAmount();
        originalDate = transaction.getDate();
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
    public int getRepeatUnitIndex() {
//        if (transaction == null || transaction.getRepeatInterval() == null) {
//            Log.d("getRepeatingIndex", "null");
            return 0;
//        } else {
//            Log.d("getRepeatingIndex", "" + transaction.getRepeatWeek());
//            return transaction.getRepeatWeek() ? 1 : 2;
//        }
    }
    public void setRepeatUnitIndex(int repeatingIndex) {
//        Log.d("repeatingIndex", "" + repeatingIndex);
//        switch (repeatingIndex) {
//            case 0:
//                transaction.setRepeatInterval(null);
//                notifyPropertyChanged(BR.repeatUnitIndex);
//                break;
//            case 1:
//                transaction.setRepeatWeek(true);
//                notifyPropertyChanged(BR.repeatUnitIndex);
//                break;
//            case 2:
//                transaction.setRepeatWeek(false);
//                notifyPropertyChanged(BR.repeatUnitIndex);
//                break;
//            default:
//                Log.e("repeatingIndex", "Illegal repeating index: " + repeatingIndex);
//                break;
//        }
    }

    @Bindable
    public String getRepeatInterval() {
//        if (transaction == null || transaction.getRepeatInterval() == null) {
            return "0";
//        } else {
//            return String.valueOf(transaction.getRepeatInterval());
//        }
    }
    public void setRepeatInterval(String repeatInterval) {
//        Log.d("repeatInterval", "" + repeatInterval);
//        if (transaction != null) {
//            try {
//                long value = Long.parseLong(repeatInterval);
//                transaction.setRepeatInterval(value);
//                notifyPropertyChanged(BR.repeatInterval);
//            } catch (NumberFormatException ex) {
//                Log.e("repeatInterval", "Error parsing number! " + repeatInterval);
//            }
//        }
    }

    @Bindable
    public String getRepeatEndDateString() {
//        if (transaction == null) {
            return "-";
//        }
//        if (transaction.getRepeatEnd() == null) {
//            return application.getResources().getString(R.string.forever);
//        } else {
//            return transaction.getRepeatEnd().toString();
//        }
    }
    public void setRepeatEndDate(LocalDate endDate) {
//        if (endDate != null) {
//            transaction.setRepeatEnd(endDate);
//            notifyPropertyChanged(BR.repeatEndDateString);
//        }
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

    public void cancel() {
        transaction.setName(originalName);
        transaction.setAccountId(originalAccountId);
        transaction.setCategoryId(originalCategoryId);
        transaction.setAmount(originalAmount);
        transaction.setDate(originalDate);
    }
}

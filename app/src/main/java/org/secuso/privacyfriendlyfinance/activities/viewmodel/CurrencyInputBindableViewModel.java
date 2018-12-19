package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

public abstract class CurrencyInputBindableViewModel extends BindableViewModel {
    public CurrencyInputBindableViewModel(@NonNull Application application) {
        super(application);
    }

    @Bindable
    public String getAmount() {
        return CurrencyHelper.convertToString(getNumericAmount());
    }
    public void setAmount(String amount) {
        if (amount == null) amount = "";
        Long number = CurrencyHelper.convertToLong(amount);
        if (number == null) number = 0L;
        if (getExpense()) number = -number;
        if (getNumericAmount() != number) {
            setNumericAmount(number);
            notifyPropertyChanged(BR.amount);
        }
    }

    @Bindable
    public int getAmountColor() {
        return getExpense() ? R.color.red : R.color.green;
    }

    @Bindable
    public boolean getExpense() {
        return getNumericAmount() <= 0;
    }
    public void setExpense(boolean checked) {
        if ((getNumericAmount() > 0 && checked) || (getNumericAmount() < 0 && !checked)) {
            setNumericAmount(getNumericAmount() * -1);
            notifyPropertyChanged(BR.expense);
            notifyPropertyChanged(BR.amountColor);
        }
    }

    protected abstract Long getNumericAmount();
    protected abstract void setNumericAmount(Long amount);
}

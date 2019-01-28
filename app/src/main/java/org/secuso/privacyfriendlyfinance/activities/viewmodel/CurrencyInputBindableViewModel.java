package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.databinding.Bindable;
import android.graphics.Color;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

public abstract class CurrencyInputBindableViewModel extends BindableViewModel {
    public CurrencyInputBindableViewModel(@NonNull Application application) {
        super(application);
    }
    private int positiveColor = Color.GREEN;
    private int negativeColor = Color.RED;


    @Bindable
    public String getAmount() {
        return CurrencyHelper.convertToString(getNumericAmount());
    }
    public void setAmount(String amount) {
        if (amount == null) amount = "";
        Long number = CurrencyHelper.convertToLong(amount);
        if (number == null) number = 0L;

        if (getNumericAmount() != number) {
            setNumericAmount(number);
            notifyPropertyChanged(BR.expense);
            notifyPropertyChanged(BR.amount);
            notifyPropertyChanged(BR.amountColor);
        }
    }

    @Bindable
    public int getAmountColor() {
        return getExpense() ? negativeColor : positiveColor;
    }

    @Bindable
    public boolean getExpense() {
        if (getNumericAmount() == null) return false;
        return getNumericAmount() <= 0;
    }
    public void setExpense(boolean checked) {
        if ((getNumericAmount() > 0 && checked) || (getNumericAmount() < 0 && !checked)) {
            setAmount(CurrencyHelper.convertToString(getNumericAmount() * -1));
            notifyPropertyChanged(BR.expense);
            notifyPropertyChanged(BR.amountColor);
        }
    }

    protected abstract Long getNumericAmount();
    protected abstract void setNumericAmount(Long amount);

    public void setCurrencyColors(int positiveColor, int negativeColor) {
        this.positiveColor = positiveColor;
        this.negativeColor = negativeColor;
        notifyPropertyChanged(BR.amountColor);
    }
}

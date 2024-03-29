/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.RepeatingTransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

/**
 * View model for the repeating transaction activity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class RepeatingTransactionsViewModel extends BaseViewModel {
    private final RepeatingTransactionDao repeatingTransactionDao =
            FinanceDatabase.getInstance(getApplication()).repeatingTransactionDao();
    private final LiveData<List<RepeatingTransaction>> repeatingTransactions;

    public RepeatingTransactionsViewModel(@NonNull Application application) {
        super(application);
        setNavigationDrawerId(R.id.nav_repeating_transactions);
        setTitle(R.string.activity_repeating_transactions_title);
        repeatingTransactions = repeatingTransactionDao.getAll();
    }

    public LiveData<List<RepeatingTransaction>> getRepeatingTransactions() {
        return repeatingTransactions;
    }

    public LiveData<List<RepeatingTransaction>> getRepeatingTransactionsFiltered(String filter) {
        return repeatingTransactionDao.getAllFiltered(filter);
    }
}

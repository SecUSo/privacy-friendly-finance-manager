package org.secuso.privacyfriendlyfinance.domain;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import org.secuso.privacyfriendlyfinance.domain.access.TransactionDao;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

@Database(entities = {Transaction.class}, version = 1)
public abstract class FinanceDatabase extends RoomDatabase {
    public abstract TransactionDao transactionDao();

}

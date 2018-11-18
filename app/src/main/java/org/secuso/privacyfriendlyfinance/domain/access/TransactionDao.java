package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

@Dao
public abstract class TransactionDao extends AbstractDao<Transaction> {
    @Override
    @Query("SELECT * FROM Tranzaction WHERE id=:id")
    public abstract Transaction get(long id);

    @Override
    @Query("SELECT * FROM Tranzaction")
    public abstract List<Transaction> getAll();
}

package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM Tranzaction")
    List<Transaction> getAll();

    @Insert
    long insert(Transaction transaction);
}

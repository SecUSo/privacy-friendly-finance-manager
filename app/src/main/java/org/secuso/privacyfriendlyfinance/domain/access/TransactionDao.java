package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

@Dao
public abstract class TransactionDao extends AbstractDao<Transaction> {
    @Override
    @Query("SELECT * FROM Tranzaction WHERE id=:id")
    public abstract LiveData<Transaction> get(long id);

    @Override
    @Query("SELECT * FROM Tranzaction ORDER BY date DESC")
    public abstract LiveData<List<Transaction>> getAll();

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId ORDER BY date DESC")
    public abstract List<Transaction> getForAccount(long accountId);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND date>=:date ORDER BY date DESC")
    public abstract List<Transaction> getForAccountFrom(long accountId, String date);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND date<:date ORDER BY date DESC")
    public abstract List<Transaction> getForAccountBefore(long accountId, String date);

    @Query("SELECT * FROM Tranzaction WHERE categoryId=:categoryId ORDER BY date DESC")
    public abstract List<Transaction> getForCategory(long categoryId);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND categoryId=:categoryId ORDER BY date DESC")
    public abstract List<Transaction> getForAccountAndCategory(long accountId, long categoryId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId")
    public abstract long sumForAccount(long accountId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId AND date>=:date")
    public abstract long sumForAccountFrom(long accountId, String date);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId AND date<:date")
    public abstract long sumForAccountBefore(long accountId, String date);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId")
    public abstract long sumForCategory(long categoryId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId AND categoryId=:categoryId")
    public abstract long sumForAccountAndCategory(long accountId, long categoryId);


}

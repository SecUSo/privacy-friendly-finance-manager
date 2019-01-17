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
    public abstract LiveData<List<Transaction>> getForAccount(long accountId);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND date>=:date ORDER BY date DESC")
    public abstract LiveData<List<Transaction>> getForAccountFrom(long accountId, String date);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND date<:date ORDER BY date DESC")
    public abstract LiveData<List<Transaction>> getForAccountBefore(long accountId, String date);

    @Query("SELECT * FROM Tranzaction WHERE categoryId=:categoryId ORDER BY date DESC")
    public abstract LiveData<List<Transaction>> getForCategory(long categoryId);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND categoryId=:categoryId ORDER BY date DESC")
    public abstract LiveData<List<Transaction>> getForAccountAndCategory(long accountId, long categoryId);



    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND amount>0")
    public abstract LiveData<Long> sumIncomeForCategory(long categoryId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND amount<0")
    public abstract LiveData<Long> sumExpensesForCategory(long categoryId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId")
    public abstract LiveData<Long> sumForAccount(long accountId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId AND date>=:date")
    public abstract LiveData<Long> sumForAccountFrom(long accountId, String date);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId AND date<:date")
    public abstract LiveData<Long> sumForAccountBefore(long accountId, String date);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId")
    public abstract LiveData<Long> sumForCategory(long categoryId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE accountId=:accountId AND categoryId=:categoryId")
    public abstract LiveData<Long> sumForAccountAndCategory(long accountId, long categoryId);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND date>=:date")
    public abstract LiveData<Long> sumForCategoryFrom(long categoryId, String date);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND amount > 0 AND date>=:date")
    public abstract LiveData<Long> sumIncomeForCategoryFrom(long categoryId, String date);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND amount < 0 AND date>=:date")
    public abstract LiveData<Long> sumExpensesForCategoryFrom(long categoryId, String date);
}

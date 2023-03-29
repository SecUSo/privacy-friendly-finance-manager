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

package org.secuso.privacyfriendlyfinance.domain.access;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.util.List;

/**
 * Data access object for transactions.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
@Dao
public abstract class TransactionDao extends AbstractDao<Transaction> {
    /*
     * L I S T S _ O F _ T R A N S A C T I O N S
     */
    @Override
    @Query("SELECT * FROM Tranzaction WHERE id=:id")
    public abstract LiveData<Transaction> get(long id);

    @Override
    @Query("SELECT * FROM Tranzaction ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getAll();

    @Query("SELECT * FROM Tranzaction WHERE name LIKE '%' || :filter || '%' ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getAllFiltered(String filter);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForAccount(long accountId);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND name LIKE '%' || :filter || '%' ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForAccountFiltered(long accountId, String filter);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND date>=:date ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForAccountFrom(long accountId, String date);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND date<:date ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForAccountBefore(long accountId, String date);

    @Query("SELECT * FROM Tranzaction WHERE categoryId=:categoryId ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForCategory(long categoryId);

    @Query("SELECT * FROM Tranzaction WHERE categoryId=:categoryId AND name LIKE '%' || :filter || '%' ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForCategoryFiltered(long categoryId, String filter);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND categoryId=:categoryId ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getForAccountAndCategory(long accountId, long categoryId);

    @Query("SELECT DISTINCT TRIM(name) FROM Tranzaction ORDER BY date DESC")
    public abstract LiveData<List<String>> getAllDistinctTitles();

    /*
     * R E P E A T I N G _ T R A N S A C T I O N S
     */
    @Query("SELECT * FROM Tranzaction WHERE repeatingId=:repeatingId ORDER BY date DESC, id DESC")
    public abstract LiveData<List<Transaction>> getByRepeatingId(long repeatingId);


    /*
     * S U M S
     */
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

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND date>=:from and date<:before")
    public abstract LiveData<Long> sumForCategoryFromBefore(long categoryId, String from, String before);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND amount > 0 AND date>=:from and date<:before")
    public abstract LiveData<Long> sumIncomeForCategoryFromBefore(long categoryId, String from, String before);

    @Query("SELECT SUM(amount) FROM Tranzaction WHERE categoryId=:categoryId AND amount < 0 AND date>=:from and date<:before")
    public abstract LiveData<Long> sumExpensesForCategoryFromBefore(long categoryId, String from, String before);

    public LiveData<Long> sumForCategoryThisMonth(long categoryId) {
        return sumForCategoryFromBefore(categoryId, LocalDate.now().withDayOfMonth(1).toString(), LocalDate.now().withDayOfMonth(1).plusMonths(1).toString());
    }

    public LiveData<Long> sumIncomeForCategoryThisMonth(long categoryId) {
        return sumIncomeForCategoryFromBefore(categoryId, LocalDate.now().withDayOfMonth(1).toString(), LocalDate.now().withDayOfMonth(1).plusMonths(1).toString());
    }

    public LiveData<Long> sumExpensesForCategoryThisMonth(long categoryId) {
        return sumExpensesForCategoryFromBefore(categoryId, LocalDate.now().withDayOfMonth(1).toString(), LocalDate.now().withDayOfMonth(1).plusMonths(1).toString());
    }

    public List<Transaction> findSameTransaction(Transaction t) {
        // String name, long amount, LocalDate date, long accountId, Long categoryId
        String name = t.getName();
        Long categoryId = t.getCategoryId();
        if (name == null) name = "";
        if (categoryId == null) categoryId = 0L;
        return findSameTransaction(name, t.getAmount(), t.getDate(), t.getAccountId(), categoryId);
    }

    // note amount,accountId and date cannot be null
    @Query("SELECT * FROM Tranzaction WHERE " +
            "IFNULL(name,'') =:name " +
            "AND amount =:amount " +
            "AND date =:date " +
            "AND accountId =:accountId " +
            "AND IFNULL(categoryId,0) =:categoryId "
    )
    public abstract List<Transaction> findSameTransaction(String name, long amount, LocalDate date, long accountId, Long categoryId);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND name=:name AND date>=:from AND date<:before ORDER BY date DESC, id DESC LIMIT 1")
    public abstract LiveData<Transaction> getLatestByNameAndAccountFromBefore(long accountId, String name, String from, String before);

    @Query("SELECT * FROM Tranzaction WHERE accountId=:accountId AND name=:name AND date>=:from AND date<:before ORDER BY date DESC, id DESC LIMIT 1")
    public abstract Transaction getLatestByNameAndAccountFromBeforeSync(long accountId, String name, String from, String before);

    public LiveData<Transaction> getLatestByNameForAccountLastMonth(Long accountId, String name) {
        return getLatestByNameAndAccountFromBefore(accountId, name, LocalDate.now().minusMonths(1).withDayOfMonth(1).toString(), LocalDate.now().withDayOfMonth(1).toString());
    }

    public CommunicantAsyncTask<?, Transaction> getLatestByNameForAccountLastMonthAsync(final Long accountId, final String name, TaskListener listener) {
        return listenAndExec(new CommunicantAsyncTask<Void, Transaction>() {
            @Override
            protected Transaction doInBackground(Void... voids) {
                return getLatestByNameAndAccountFromBeforeSync(accountId, name, LocalDate.now().minusMonths(1).withDayOfMonth(1).toString(), LocalDate.now().withDayOfMonth(1).toString());
            }
        }, listener);
    }
}

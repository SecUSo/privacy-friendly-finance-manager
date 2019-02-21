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

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.joda.time.LocalDate;
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


    /*
     * R E P E A T I N G _ T R A N S A C T I O N S
     */
    @Query("SELECT * FROM Tranzaction WHERE repeatingId=:repeatingId ORDER BY date DESC")
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
}

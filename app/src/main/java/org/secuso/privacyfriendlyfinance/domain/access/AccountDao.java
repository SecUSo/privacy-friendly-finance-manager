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

import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

/**
 * Data access object for accounts.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
@Dao
public abstract class AccountDao extends AbstractDao<Account> {
    @Query("SELECT COUNT(*) FROM Account")
    public abstract long count();

    @Override
    @Query("SELECT * FROM Account WHERE id=:id")
    public abstract LiveData<Account> get(long id);

    @Query("SELECT * FROM Account WHERE rowid = :rowId")
    public abstract Account getByRowId(long rowId);

    @Query("SELECT * FROM Account WHERE name LIKE :name")
    public abstract LiveData<Account> getByName(String name);

    @Override
    @Query("SELECT * FROM Account")
    public abstract LiveData<List<Account>> getAll();
    @Query("SELECT * FROM Account")
    public abstract List<Account> getAllSynchron();
}
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

import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

/**
 * @author Felix Hofmann
 * @author Leonard Otto
 */
@Dao
public abstract class RepeatingTransactionDao extends AbstractDao<RepeatingTransaction> {
    @Override
    @Query("SELECT * FROM RepeatingTransaction WHERE id=:id")
    public abstract LiveData<RepeatingTransaction> get(long id);

    @Override
    @Query("SELECT * FROM RepeatingTransaction ORDER BY id ASC")
    public abstract LiveData<List<RepeatingTransaction>> getAll();

    @Query("SELECT * FROM RepeatingTransaction ORDER BY id ASC")
    public abstract List<RepeatingTransaction> getAllSync();
}

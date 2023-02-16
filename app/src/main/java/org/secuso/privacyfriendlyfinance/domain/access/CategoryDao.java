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
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

/**
 * Data access object for categories.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
@Dao
public abstract class CategoryDao extends AbstractDao<Category> {
    @Override
    @Query("SELECT * FROM Category WHERE id=:id ORDER BY name")
    public abstract LiveData<Category> get(long id);

    @Query("SELECT * FROM Category WHERE rowid = :rowId")
    public abstract Category getByRowId(long rowId);

    @Query("SELECT * FROM Category WHERE name LIKE :name")
    public abstract Category get(String name);

    @Override
    @Query("SELECT * FROM Category  ORDER BY name")
    public abstract LiveData<List<Category>> getAll();

    @Query("SELECT * FROM Category ORDER BY name")
    public abstract List<Category> getAllSynchron();
}

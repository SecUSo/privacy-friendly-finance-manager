/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2023 MaxIsV, k3b

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

package org.secuso.privacyfriendlyfinance.csv;

import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.domain.model.common.Name2IdCreateIfNotExists;

/**
 * Android Room specific Translater from CategoryName to CategoryId that creates Category on demand.
 */
public class Category2Id extends Name2IdCreateIfNotExists<Category> {

    private final CategoryDao dao;

    public Category2Id(CategoryDao dao) {
        super(dao.getAllSynchron());
        this.dao = dao;
    }

    @Override
    protected Category createItem() {
        return new Category();
    }

    @Override
    protected Category save(Category newItem) {
        long rowId = dao.insert(newItem);
        if(rowId >= 0) {
            return dao.getByRowId(rowId);
        }
        return null;
    }
}

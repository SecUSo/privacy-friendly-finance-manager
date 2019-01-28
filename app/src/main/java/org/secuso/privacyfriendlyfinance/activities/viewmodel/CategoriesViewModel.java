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

package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.activities.adapter.CategoryWrapper;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for the categories activity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class CategoriesViewModel extends BaseViewModel {
    private LiveData<List<CategoryWrapper>> categories;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        setNavigationDrawerId(R.id.nav_category);
        setTitle(R.string.activity_categories_title);
        categories = Transformations.map(FinanceDatabase.getInstance().categoryDao().getAll(), new Function<List<Category>, List<CategoryWrapper>>() {
            @Override
            public List<CategoryWrapper> apply(List<Category> input) {
                List<CategoryWrapper> wrappers = new ArrayList<>();
                for (Category category : input) {
                    wrappers.add(new CategoryWrapper(category));
                }
                return wrappers;
            }
        });
    }

    public LiveData<List<CategoryWrapper>> getCategories() {
        return categories;
    }
}

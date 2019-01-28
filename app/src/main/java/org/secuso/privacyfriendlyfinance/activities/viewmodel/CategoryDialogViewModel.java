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
import android.arch.lifecycle.LiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;
import org.secuso.privacyfriendlyfinance.helpers.CurrencyHelper;

import java.util.List;

/**
 * View model for the category dialog.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class CategoryDialogViewModel extends BindableViewModel {
    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();
    private Category category;
    private String originalName;
    private Integer originalColor;
    private Long originalBudget;

    public CategoryDialogViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAll();
    }


    public void setCategoryId(long categoryId) {
        category = categoryId == -1 ? new Category() : categoryDao.getCached(categoryId);
        originalName = category.getName();
        originalColor = category.getColor();
        originalBudget = category.getBudget();
    }

    public boolean isNewCategory() {
        return category.getId() == null;
    }

    @Bindable
    public String getName() {
        return category.getName();
    }
    public void setName(String name) {
        if (name == null) name = "";
        if (category.getName() == null) category.setName("");
        if (!category.getName().equals(name)) {
            category.setName(name);
            notifyPropertyChanged(BR.name);
        }
    }

    @Bindable
    public String getBudget() {
        return  CurrencyHelper.convertToString(category.getBudget());
    }
    public void setBudget(String budget) {
        if (budget == null) budget = "";
        category.setBudget(CurrencyHelper.convertToLong(budget));
    }

    @Bindable
    public Integer getColor() {
        return category.getColor();
    }
    public void setColor(Integer color) {
        if (category.getColor() != color) {
            category.setColor(color);
            notifyPropertyChanged(BR.color);
        }
    }

    public void submit() {
        categoryDao.updateOrInsertAsync(category);
    }

    public void cancel() {
        category.setName(originalName);
        category.setColor(originalColor);
        category.setBudget(originalBudget);
    }

    public Category getCategory() {
        return category;
    }
}

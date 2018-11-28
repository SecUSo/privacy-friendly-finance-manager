package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

public class CategoriesViewModel extends ViewModel {
    private LiveData<List<Category>> categories = FinanceDatabase.getInstance().categoryDao().getAll();

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void deleteCategory(Category category) {
        categories.getValue().remove(category);
        //TODO: notify someone?
    }

    public void updateOrInsertCategory(Category category) {
        //TODO: how?
    }
}

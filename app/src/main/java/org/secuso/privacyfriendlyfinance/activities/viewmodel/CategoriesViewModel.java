package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.R;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

public class CategoriesViewModel extends BaseViewModel {
    private CategoryDao categoryDao = FinanceDatabase.getInstance().categoryDao();

    private LiveData<List<Category>> categories = categoryDao.getAll();

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        setNavigationDrawerId(R.id.nav_category);
    }

    public LiveData<List<Category>> getAllCategories() {
        return categories;
    }

    public void deleteCategory(Category category) {
        categoryDao.deleteAsync(category);
    }

    public void updateOrInsertCategory(Category category) {
        categoryDao.updateOrInsertAsync(category);
    }
}

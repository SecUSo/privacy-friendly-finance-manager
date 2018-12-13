package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

public class CategoryDialogViewModel extends AndroidViewModel {
    private CategoryDao categoryDao;

    public CategoryDialogViewModel(@NonNull Application application) {
        super(application);
        categoryDao = FinanceDatabase.getInstance().categoryDao();
    }

    public LiveData<Category> getCategoryById(long id) {
        return categoryDao.get(id);
    }

    public void updateOrInsert(Category category) {
        categoryDao.updateOrInsertAsync(category);
    }
}

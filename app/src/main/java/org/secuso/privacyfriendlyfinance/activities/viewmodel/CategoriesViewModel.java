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

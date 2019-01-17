package org.secuso.privacyfriendlyfinance.activities.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import org.secuso.privacyfriendlyfinance.BR;
import org.secuso.privacyfriendlyfinance.domain.FinanceDatabase;
import org.secuso.privacyfriendlyfinance.domain.access.CategoryDao;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

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

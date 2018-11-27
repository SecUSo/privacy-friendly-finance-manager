package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

@Dao
public abstract class CategoryDao extends AbstractDao<Category> {
    @Override
    @Query("SELECT * FROM Category WHERE id=:id")
    public abstract LiveData<Category> get(long id);

    @Query("SELECT * FROM Category WHERE name LIKE :name")
    public abstract LiveData<Category> getByName(String name);

    @Override
    @Query("SELECT * FROM Category")
    public abstract LiveData<List<Category>> getAll();
}

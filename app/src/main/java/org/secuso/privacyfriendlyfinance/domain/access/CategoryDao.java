package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Insert
    long insert(Category category);

    /**
     * Update the given category object. The id should be unchanged.
     *
     * @param category
     * @return
     */
    @Update
    long updateCategory(Category category);
}

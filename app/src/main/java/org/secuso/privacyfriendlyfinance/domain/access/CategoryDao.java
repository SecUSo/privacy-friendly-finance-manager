package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

@Dao
public abstract class CategoryDao {
    @Query("SELECT * FROM Category")
    public abstract List<Category> getAll();

    @Query("SELECT * FROM Category WHERE id=:id")
    public abstract Category get(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(Category entity);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public abstract void update(Category entity);

    public long upsert(Category entity) {
        long id = insert(entity);
        if (id == -1) {
            update(entity);
            id = entity.getId();
        }
        return id;
    }

    public CommunicantAsyncTask<?, List<Category>> getAllAsync(TaskListener listener) {
        CommunicantAsyncTask<Void, List<Category>> task = new CommunicantAsyncTask<Void, List<Category>>() {
            @Override
            protected List<Category> doInBackground(Void... voids) {
                return getAll();
            }
        };
        if (listener != null) task.addListener(listener);
        task.execute();
        return task;
    }

    public CommunicantAsyncTask<?, Long> upsertAsync(final Category entity, TaskListener listener) {
        CommunicantAsyncTask<Void, Long> task = new CommunicantAsyncTask<Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return upsert(entity);
            }
        };
        if (listener != null) task.addListener(listener);
        task.execute();
        return task;
    }
}

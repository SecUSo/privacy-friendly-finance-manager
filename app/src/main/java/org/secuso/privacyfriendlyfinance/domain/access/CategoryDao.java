package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.Category;

import java.util.List;

@Dao
public abstract class CategoryDao extends AbstractDao<Category> {
    @Query("SELECT * FROM Category")
    public abstract List<Category> getAll();

    @Query("SELECT * FROM Category WHERE id=:id")
    public abstract Category get(long id);

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
}

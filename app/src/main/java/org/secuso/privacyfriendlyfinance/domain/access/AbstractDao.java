package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.AbstractEntity;


public abstract class AbstractDao<E extends AbstractEntity> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insert(E entity);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public abstract void update(E entity);

    public long updateOrInsert(E entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            update(entity);
            return entity.getId();
        }
    }

    public CommunicantAsyncTask<?, Long> updateOrInsertAsync(final E entity, TaskListener listener) {
        CommunicantAsyncTask<Void, Long> task = new CommunicantAsyncTask<Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return updateOrInsert(entity);
            }
        };
        if (listener != null) task.addListener(listener);
        task.execute();
        return task;
    }
}

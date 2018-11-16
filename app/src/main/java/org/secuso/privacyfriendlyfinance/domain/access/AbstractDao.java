package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.AbstractEntity;

import java.util.List;


public abstract class AbstractDao<E extends AbstractEntity> {
    public abstract E get(long id);
    public abstract List<E> getAll();

    @Insert
    public abstract long insert(E entity);

    @Update
    public abstract void update(E entity);

    @Delete
    public abstract void delete(E entity);

    public long updateOrInsert(E entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            update(entity);
            return entity.getId();
        }
    }

    protected final <T> CommunicantAsyncTask<Void, T> listenAndExec(CommunicantAsyncTask<Void, T> task, TaskListener listener) {
        if (listener != null) task.addListener(listener);
        task.execute();
        return task;
    }

    public CommunicantAsyncTask<?, Long> updateOrInsertAsync(final E entity, TaskListener listener) {
        CommunicantAsyncTask<Void, Long> task = new CommunicantAsyncTask<Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return updateOrInsert(entity);
            }
        };
        return listenAndExec(task, listener);
    }

    public CommunicantAsyncTask<?, E> getAsync(final long id, TaskListener listener) {
        final AbstractDao<E> t = this;
        return listenAndExec(new CommunicantAsyncTask<Void, E>() {
            @Override
            protected E doInBackground(Void... voids) {
                return t.get(id);
            }
        }, listener);
    }

    public CommunicantAsyncTask<?, List<E>> getAllAsync(TaskListener listener) {
        return listenAndExec(new CommunicantAsyncTask<Void, List<E>>() {
            @Override
            protected List<E> doInBackground(Void... voids) {
                return getAll();
            }
        }, listener);
    }

    public CommunicantAsyncTask<?, Void> deleteAsync(final E entity, TaskListener listener) {
        return listenAndExec(new CommunicantAsyncTask<Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                delete(entity);
                return null;
            }
        }, listener);
    }
    public CommunicantAsyncTask<?, Void> deleteAsync(final E entity) {
        return deleteAsync(entity, null);
    }
}

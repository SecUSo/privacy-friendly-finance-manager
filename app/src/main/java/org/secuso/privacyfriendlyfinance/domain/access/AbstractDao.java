/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.domain.access;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import org.secuso.privacyfriendlyfinance.activities.helper.CommunicantAsyncTask;
import org.secuso.privacyfriendlyfinance.activities.helper.TaskListener;
import org.secuso.privacyfriendlyfinance.domain.model.AbstractEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Data access base class used to access all entities in the database that inherit from
 * AbstractEntity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public abstract class AbstractDao<E extends AbstractEntity> {
    public abstract LiveData<E> get(long id);
    public abstract LiveData<List<E>> getAll();

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
    public CommunicantAsyncTask<?, Long> updateOrInsertAsync(final E entity) {
        return updateOrInsertAsync(entity, null);
    }

    public CommunicantAsyncTask<?, LiveData<E>> getAsync(final long id, TaskListener listener) {
        final AbstractDao<E> t = this;
        return listenAndExec(new CommunicantAsyncTask<Void, LiveData<E>>() {
            @Override
            protected LiveData<E> doInBackground(Void... voids) {
                return t.get(id);
            }
        }, listener);
    }

    public CommunicantAsyncTask<?, LiveData<List<E>>> getAllAsync(TaskListener listener) {
        return listenAndExec(new CommunicantAsyncTask<Void, LiveData<List<E>>>() {
            @Override
            protected LiveData<List<E>> doInBackground(Void... voids) {
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

    public LiveData<Map<Long, E>> getAllMap() {
        return Transformations.map(getAll(), new Function<List<E>, Map<Long, E>>() {
            @Override
            public Map<Long, E> apply(List<E> input) {
                Map<Long, E> map = new HashMap<>();
                for (E entity : input) {
                    map.put(entity.getId(), entity);
                }
                return map;
            }
        });
    }
}

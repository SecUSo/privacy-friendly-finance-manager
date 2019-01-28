package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.RepeatingTransaction;

import java.util.List;

@Dao
public abstract class RepeatingTransactionDao extends AbstractDao<RepeatingTransaction> {
    @Override
    @Query("SELECT * FROM RepeatingTransaction WHERE id=:id")
    public abstract LiveData<RepeatingTransaction> get(long id);

    @Override
    @Query("SELECT * FROM RepeatingTransaction ORDER BY id ASC")
    public abstract LiveData<List<RepeatingTransaction>> getAll();

    @Query("SELECT * FROM RepeatingTransaction ORDER BY id ASC")
    public abstract List<RepeatingTransaction> getAllSync();
}

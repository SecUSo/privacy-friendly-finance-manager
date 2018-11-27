package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

@Dao
public abstract class AccountDao extends AbstractDao<Account> {
    @Query("SELECT COUNT(*) FROM Account")
    public abstract long count();

    @Override
    @Query("SELECT * FROM Account WHERE id=:id")
    public abstract LiveData<Account> get(long id);

    @Query("SELECT * FROM Account WHERE name LIKE :name")
    public abstract LiveData<Account> getByName(String name);

    @Override
    @Query("SELECT * FROM Account")
    public abstract LiveData<List<Account>> getAll();
}
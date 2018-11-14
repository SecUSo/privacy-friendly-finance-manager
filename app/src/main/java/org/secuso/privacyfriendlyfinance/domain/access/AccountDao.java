package org.secuso.privacyfriendlyfinance.domain.access;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.secuso.privacyfriendlyfinance.domain.model.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM Account")
    List<Account> getAll();

    @Insert
    long insert(Account account);
}

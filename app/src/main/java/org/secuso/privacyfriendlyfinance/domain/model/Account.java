package org.secuso.privacyfriendlyfinance.domain.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private long initialBalance;

    public Account() {
    }

    @Ignore
    public Account(long id, String name, long initialBalance) {
        this.id = id;
        this.name = name;
        this.initialBalance = initialBalance;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getInitialBalance() {
        return initialBalance;
    }
    public void setInitialBalance(long initialBalance) {
        this.initialBalance = initialBalance;
    }
}

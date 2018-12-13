package org.secuso.privacyfriendlyfinance.domain.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(
    tableName = "Account",
    inheritSuperIndices = true,
    indices = @Index(value = "name", unique = true)
)
public class Account  extends AbstractEntity {
    private String name;
    private long initialBalance = 0L;

    public Account() {
    }

    @Ignore
    public Account(String name, long initialBalance) {
        this.name = name;
        this.initialBalance = initialBalance;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public long getInitialBalance() {
        return initialBalance;
    }
    public void setInitialBalance(long initialBalance) {
        this.initialBalance = initialBalance;
    }
}

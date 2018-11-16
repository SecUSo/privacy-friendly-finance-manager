package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Tranzaction")
public class Transaction extends AbstractEntity {
    private String name;
    private long amount;
    private String date;

    public Transaction() {
    }

    @Ignore
    public Transaction(String name, long amount, String date) {
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

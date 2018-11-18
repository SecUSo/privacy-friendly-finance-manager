package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import org.joda.time.DateTime;
import org.secuso.privacyfriendlyfinance.domain.convert.DateTimeConverter;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Tranzaction",
        foreignKeys = @ForeignKey(entity = Category.class,
                                    parentColumns = "id",
                                    childColumns = "categoryId",
                                    onDelete = CASCADE))

public class Transaction extends AbstractEntity {
    private String name;
    private long amount;
    private DateTime date;
    private long categoryId;

    public Transaction() {
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

    public DateTime getDate() {
        return date;
    }
    public void setDate(DateTime date) {
        this.date = date;
    }

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public String getDateAsString() { return DateTimeConverter.datetimeToString(date); }
}

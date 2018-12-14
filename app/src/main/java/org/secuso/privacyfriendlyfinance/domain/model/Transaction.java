package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import org.joda.time.LocalDate;

@Entity(
    tableName = "Tranzaction",
    inheritSuperIndices = true,
    indices = {
            @Index(value = "date"),
            @Index(value = "categoryId"),
            @Index(value = "accountId"),
    },
    foreignKeys = {
        @ForeignKey(
            entity = Category.class,
            parentColumns = "id",
            childColumns = "categoryId",
            onDelete = ForeignKey.SET_NULL
        ),
        @ForeignKey(
            entity = Account.class,
            parentColumns = "id",
            childColumns = "accountId",
            onDelete = ForeignKey.CASCADE
        ),
    }
)
public class Transaction extends AbstractEntity {
    private String name;
    private long amount;
    private LocalDate date = LocalDate.now();

    private long accountId;
    private Long categoryId;

    public Transaction() {
    }
    @Ignore
    public Transaction(String name, long amount, LocalDate date, long accountId) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.accountId = accountId;
    }
    @Ignore
    public Transaction(String name, long amount, LocalDate date, long accountId, Long categoryId) {
        this(name, amount, date, accountId);
        this.categoryId = categoryId;
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

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        if (date != null) this.date = date;
    }

    public long getAccountId() {
        return accountId;
    }
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        if (this.categoryId == null || this.categoryId < 0) this.categoryId = null;
    }
}

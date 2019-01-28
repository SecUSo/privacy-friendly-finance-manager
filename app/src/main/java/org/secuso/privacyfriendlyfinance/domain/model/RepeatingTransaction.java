package org.secuso.privacyfriendlyfinance.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import org.joda.time.LocalDate;

@Entity(
        tableName = "RepeatingTransaction",
        inheritSuperIndices = true,
        indices = {
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
public class RepeatingTransaction extends AbstractEntity {
    private String name;
    private long amount;
    private LocalDate latestInsert = LocalDate.now();
    private LocalDate end;

    private long accountId;
    private Long categoryId;

    private long interval = 1L;
    private boolean weekly = false;

    public Transaction getTransaction() {
        Transaction result = new Transaction();

        result.setRepeatingId(getId());
        result.setName(name);
        result.setAccountId(accountId);
        result.setCategoryId(categoryId);
        result.setAmount(amount);
        result.setDate(LocalDate.now());

        return result;
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

    public LocalDate getLatestInsert() {
        return latestInsert;
    }

    public void setLatestInsert(LocalDate latestInsert) {
        this.latestInsert = latestInsert;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
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
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public boolean isWeekly() {
        return weekly;
    }

    public void setWeekly(boolean weekly) {
        this.weekly = weekly;
    }
}

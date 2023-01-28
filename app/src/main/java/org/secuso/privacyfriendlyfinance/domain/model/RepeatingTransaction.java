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

package org.secuso.privacyfriendlyfinance.domain.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.model.common.NameWithIdProvider;

/**
 * Repeating transaction entity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
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
public class RepeatingTransaction extends AbstractEntity implements NameWithIdProvider {
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

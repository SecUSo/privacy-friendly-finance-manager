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
import androidx.room.Ignore;
import androidx.room.Index;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.model.common.NameWithIdProvider;

/**
 * Transaction entity.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
@Entity(
    tableName = "Tranzaction",
    inheritSuperIndices = true,
    indices = {
            @Index(value = "date"),
            @Index(value = "categoryId"),
            @Index(value = "accountId"),
            @Index(value = "repeatingId"),
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
        @ForeignKey(
            entity = RepeatingTransaction.class,
            parentColumns = "id",
            childColumns = "repeatingId",
            onDelete = ForeignKey.SET_NULL
        ),
    }
)
public class Transaction extends AbstractEntity implements NameWithIdProvider {
    private String name;
    private long amount;
    private LocalDate date = LocalDate.now();

    private long accountId;
    private Long categoryId;

    private Long repeatingId;

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

    public Long getRepeatingId() {
        return repeatingId;
    }

    public void setRepeatingId(Long repeatingId) {
        this.repeatingId = repeatingId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("name='").append(name).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", date=").append(date);
        sb.append(", accountId=").append(accountId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", repeatingId=").append(repeatingId);
        sb.append('}');
        return sb.toString();
    }
}

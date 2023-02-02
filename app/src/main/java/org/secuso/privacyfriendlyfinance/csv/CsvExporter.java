/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019-2023 Leonard Otto, Felix Hofmann, k3b

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

package org.secuso.privacyfriendlyfinance.csv;

import androidx.annotation.NonNull;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;

/**
 * converts (list of) {@link Transaction} items to csv format used for export.
 */
public class CsvExporter {
    public static final String CSV_FIELD_DELIMITER = ";";
    private final Id2Name<?> id2Category;
    private final Id2Name<?> id2Account;

    public CsvExporter(Id2Name<?> id2Category, Id2Name<?> id2Account) {
        this.id2Category = id2Category;
        this.id2Account = id2Account;
    }

    public String toCsv(Transaction transaction) {

        return toCsv(transaction.getDate(), transaction.getAmount(), transaction.getName(),
                id2Category.get(transaction.getCategoryId()), id2Account.get(transaction.getAccountId()));
    }

    public String createCsvHeader() {
        return toCsv("date","amount","note","category","account");
    }

    @NonNull
    private String toCsv(Object date, Object amount, Object comment, Object categoryName, Object accountName) {
        return date + CSV_FIELD_DELIMITER + amount + CSV_FIELD_DELIMITER + comment
                + CSV_FIELD_DELIMITER + categoryName + CSV_FIELD_DELIMITER + accountName;
    }
}

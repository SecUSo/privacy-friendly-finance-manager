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

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * converts (list of) {@link Transaction} items to csv format used for export.
 */
public class CsvExporter implements AutoCloseable {
    public static final char CSV_FIELD_DELIMITER_CHAR = ';';
    public static final String CSV_FIELD_DELIMITER = "" + CSV_FIELD_DELIMITER_CHAR;
    private final Id2Name<?> id2Category;
    private final Id2Name<?> id2Account;

    ICSVWriter csvWriter;

    public CsvExporter(Writer resultWriter, Id2Name<?> id2Category, Id2Name<?> id2Account) {
        this.id2Category = id2Category;
        this.id2Account = id2Account;

        csvWriter = new CSVWriterBuilder(resultWriter)
                .withSeparator(CSV_FIELD_DELIMITER_CHAR)
                .build();

    }

    public void writeCsvLine(Transaction transaction) {

        writeCsvLine(
                toString(transaction.getDate()),
                toString(transaction.getAmount()),
                toString(transaction.getName()),
                toString(id2Category.get(transaction.getCategoryId())),
                toString(id2Account.get(transaction.getAccountId())));
    }

    /**
     * nullsafe: converts object to string
     */
    private String toString(Object o) {
        return o != null ? o.toString() : null;
    }

    public void writeCsvHeader() {
        writeCsvLine("date","amount","note","category","account");
    }

    @NonNull
    private void writeCsvLine(String... columns) {
        csvWriter.writeNext(columns, false);
        csvWriter.flushQuietly();
    }

    public void writeCsvLineByList(List<String[]> lines) throws IOException {
        for(String[] line : lines) {
            csvWriter.writeNext(line);
        }
        csvWriter.flush();
    }

    @Override
    public void close() throws Exception {
        csvWriter.close();
    }
}

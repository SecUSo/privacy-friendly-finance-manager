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

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private String toCsv(Object... columns) {
        return  Arrays.stream(columns)
                .map(o -> o.toString())
                .collect(Collectors.joining(CSV_FIELD_DELIMITER));
    }
    @NonNull
    private String toCsv_original (Object date, Object amount, Object comment, Object categoryName, Object accountName) {
        return date + CSV_FIELD_DELIMITER + amount + CSV_FIELD_DELIMITER + comment
                + CSV_FIELD_DELIMITER + categoryName + CSV_FIELD_DELIMITER + accountName;
    }

    public void writeData(List<String[]> lines) throws IOException {
//        Writer writer = new FileWriter("the-test-text.csv");
//
//        StatefulBeanToCsv<CSVHeader> beanToCsv = new StatefulBeanToCsvBuilder<CSVHeader>(writer)
//                .withSeparator(';')
//                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
//                .withOrderedResults(true)
//                .build();

        CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter("the-test-text.csv")).withSeparator(';').build();
        for(String[] line : lines) {
            writer.writeNext(line);
        }
        writer.close();
    }
}

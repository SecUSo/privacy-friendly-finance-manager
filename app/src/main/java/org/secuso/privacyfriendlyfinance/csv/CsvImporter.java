/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2023 MaxIsV, k3b

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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.joda.time.LocalDate;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Name2Id;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvImporter  implements AutoCloseable {

    private final CSVReader csvReader;
    private Name2Id<?> accountName2Id;
    private Name2Id<?> categoryName2Id;

    public CsvImporter(Reader csvDataReader, Name2Id<?> accountName2Id, Name2Id<?> categoryName2Id) {
        this.accountName2Id = accountName2Id;
        this.categoryName2Id = categoryName2Id;
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(CsvDefinitions.CSV_FIELD_DELIMITER_CHAR)
                .withIgnoreQuotations(true)
                .build();

        csvReader = new CSVReaderBuilder(csvDataReader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build();


    }

    public List<Transaction> readFromCsv() throws CsvValidationException, IOException {
        List<Transaction> list = new ArrayList<>();
        String[] line;

        String[] headers = csvReader.readNext();
        int columnNoNote = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_NOTE); // i.e. note content is in column 7
        int columnNoAmount = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_AMOUNT);
        int columnNoDate = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_DATE);
        int columnNoAccount = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_ACCOUNT);
        int columnNoCategory = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_CATEGORY);

        while ((line = csvReader.readNext()) != null) {
            Transaction tr = createTransaction(
                    getColumnContent(line, columnNoNote),
                    getColumnContent(line, columnNoAmount),
                    getColumnContent(line, columnNoDate),
                    getColumnContent(line, columnNoAccount),
                    getColumnContent(line, columnNoCategory));

            list.add(tr);
            System.out.println(line);
        }
        csvReader.close();
        return list;
    }

    protected Transaction createTransaction(String nameStr, String amountStr, String dateStr, String accountStr , String categoryStr) {
        Transaction tr = new Transaction();
        tr.setName(nameStr);

        tr.setAmount(floatString2Long(amountStr));
        tr.setDate(dateString2Date(dateStr));
        tr.setAccountId(idString2Acc(accountStr));
        tr.setCategoryId(idString2Cat(categoryStr));

        return tr;
    }

    protected long idString2Acc(String accountStr) {
        if (accountStr != null && !accountStr.isEmpty()) {

            try {
                return accountName2Id.get(accountStr);
            } catch (Exception ex) {
                addError("'" + accountStr +"'" +
                        "account doesn't exist:" + ex.getMessage());
                return 0;
            }
        }

        return 0;
    }

    protected Long idString2Cat(String categoryStr) {
        if (categoryStr != null && !categoryStr.isEmpty()) {

            try {
                return categoryName2Id.get(categoryStr);
            } catch (Exception ex) {
                addError("'" + categoryStr +"'" +
                        "category doesn't exist:" + ex.getMessage());
                return null;
            }
        }

        return 0L;
    }

    protected long floatString2Long(String amountStr) {
        float amount = 0.0f;

        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                amount = Float.parseFloat(amountStr) * 100;
            } catch (NumberFormatException ex) {
                addError("'" + amountStr +"'" +
                        "is not a valid amount. Valid example '123.45' :" + ex.getMessage());
                amount = 0.0f;
            }
        }
        return (long) amount;
    }

    private void addError(String s) {
        // TODO generate errormessagetext for user with current csv-line number
    }

    protected LocalDate dateString2Date(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        // cut off time info if exists asssuming that date part is always 10 chars long
        if (dateStr.length() > 10) dateStr = dateStr.substring(0,10);
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception ex) {
            addError("'" + dateStr +"'" +
                    "is not a valid date. Valid example '2022-12-14' :" + ex.getMessage());
            return null;
        }
    }

    protected int getColumnNo(String[] headers, String columnName) {

        for(int x = 0; x < headers.length; x++) {
            if(Objects.equals(headers[x], columnName)) return x;
        }

        return -1;
    }

    protected String getColumnContent(String[] line, int columnNo) {
        if(columnNo >= 0 && columnNo < line.length) return line[columnNo];
        return null;
    }


    @Override
    public void close() throws Exception {
        this.csvReader.close();
    }
}

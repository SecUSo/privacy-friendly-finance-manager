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
    private StringBuilder errors;
    private int lineNumber;

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
        this.errors = new StringBuilder();
        lineNumber = 0;
        List<Transaction> list = new ArrayList<>();
        String[] line;

        String[] headers = readNextCsvColumnLine();
        int columnNoNote = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_NOTE); // i.e. note content is in column 7
        int columnNoAmount = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_AMOUNT);
        int columnNoDate = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_DATE);
        int columnNoAccount = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_ACCOUNT);
        int columnNoCategory = getColumnNo(headers, CsvDefinitions.COLUMN_NAME_CATEGORY);

        while ((line = readNextCsvColumnLine()) != null) {
            try {
                Transaction tr = createTransaction(
                        getColumnContent(line, columnNoNote),
                        getColumnContent(line, columnNoAmount),
                        getColumnContent(line, columnNoDate),
                        getColumnContent(line, columnNoAccount),
                        getColumnContent(line, columnNoCategory));

                list.add(tr);
            } catch (Exception ex) {
                addError(ex.getMessage());
            }
            System.out.println(line);
        }
        csvReader.close();
        return list;
    }

    private String[] readNextCsvColumnLine() throws IOException, CsvValidationException {
        String[] columns;
        do {
            lineNumber++;
            columns = csvReader.readNext();
        } while (columns != null && isComment(columns));
        return columns;
    }

    // " ; ;; "
    private boolean isComment(String[] columns) {
        // empty line without content
        if (columns.length == 1 && columns[0].trim().length() == 0) return true;

        // comments start with "#" char
        return columns[0].startsWith("#");
    }

    protected Transaction createTransaction(String nameStr, String amountStr, String dateStr, String accountStr , String categoryStr) {
        Transaction tr = new Transaction();

        tr.setName(nameStr);

        tr.setAmount(floatString2Long(amountStr));
        tr.setDate(dateString2Date(dateStr));
        tr.setAccountId(idString2Acc(accountStr));

        if (categoryStr != null && !categoryStr.isEmpty() && !categoryStr.equals("0")) {
            tr.setCategoryId(idString2Cat(categoryStr));
        } // else CategoryId remains null

        return tr;
    }

    protected long idString2Acc(String accountStr) {
        if (accountStr != null && !accountStr.isEmpty()) {
            try {
                // executes sql create on demand
                return accountName2Id.get(accountStr);
            } catch (Exception ex) {
                throw new RuntimeException("'" + accountStr +"'" +
                        "account cannot be created :" + ex.getMessage(), ex);
            }
        }
        return 0;
    }

    protected Long idString2Cat(String categoryStr) {
        if (categoryStr != null && !categoryStr.isEmpty()) {

            try {
                // executes sql create on demand
                return categoryName2Id.get(categoryStr);
            } catch (Exception ex) {
                throw new RuntimeException("'" + categoryStr +"'" +
                        "category cannot be created:" + ex.getMessage(), ex);
            }
        }
        return null;
    }

    protected long floatString2Long(String amountStr) {
        float amount = 0.0f;

        if (amountStr != null && !amountStr.isEmpty()) {
                amount = Float.parseFloat(amountStr) * 100;
                // may throw exception if not a valid number
        }
        return (long) amount;
    }

    private void addError(String s) {
        errors
                .append("Error #")
                .append(lineNumber)
                .append("\n")
                .append(s)
                .append("\n");
    }

    protected LocalDate dateString2Date(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return new LocalDate(); // default to today

        // cut off time info if exists asssuming that date part is always 10 chars long
        if (dateStr.length() > 10) {
            dateStr = dateStr.substring(0,10);
        }

        return LocalDate.parse(dateStr);
    }

    protected int getColumnNo(String[] headers, String columnName) {

        for(int x = 0; x < headers.length; x++) {
            if(Objects.equals(headers[x].toLowerCase().trim(), columnName)) return x;
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

    public String getErrors() {
        return errors.toString();
    }
}

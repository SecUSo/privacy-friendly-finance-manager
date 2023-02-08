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
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvImporter  implements AutoCloseable {

    private final CSVReader csvReader;

    public CsvImporter(Reader csvDataReader) {
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

        while ((line = csvReader.readNext()) != null) {
            Transaction transaction = new Transaction();

            // date;amount;note;category;account
            // 1999-12-31;0.05;My Test Transaction;my test category;my test account

            // content of column note
            String noteColumnContent = getColumnContent(line, columnNoNote);  // i.e. "My Test Transaction";
            transaction.setName(noteColumnContent);
            
            list.add(transaction);
            System.out.println(line);
        }
        csvReader.close();
        return list;
    }

    protected int getColumnNo(String[] headers, String columnName) {

        for(int x = 0; x < headers.length; x++) {
            if(Objects.equals(headers[x], columnName)) return x;
        }

        return -1;
    }

    protected String getColumnContent(String[] line, int columnNoNote) {
        return line[columnNoNote];
    }

    @Override
    public void close() throws Exception {
        this.csvReader.close();
    }
}

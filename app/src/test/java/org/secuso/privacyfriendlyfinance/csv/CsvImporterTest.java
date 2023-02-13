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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.opencsv.exceptions.CsvValidationException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.secuso.privacyfriendlyfinance.domain.model.Account;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Name2Id;
import org.secuso.privacyfriendlyfinance.domain.model.common.Name2IdCreateIfNotExists;
import org.secuso.privacyfriendlyfinance.domain.model.common.NameWithIdDto;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class CsvImporterTest {
    @Test
    public void readCsv() throws CsvValidationException, IOException {
        // date,amount,note,category,account
        String csvData = "date;amount;note;category;account\n" +
                "1999-12-31;0.05;My Test Transaction;my test category;my test account";

        Name2Id<NameWithIdDto> account2Id = new Name2Id<>(List.of(new NameWithIdDto("my test account", 12345L)));
        Name2Id<NameWithIdDto> category2Id = new Name2Id<>(List.of(new NameWithIdDto("my test category", 54321L)));
        StringReader csvDataReader = new StringReader(csvData);

        CsvImporter importer = new CsvImporter(csvDataReader, account2Id, category2Id);

        List<Transaction> transactions = importer.readFromCsv();
        assertNotNull("contains data", transactions);
        assertEquals("exactly one element",1, transactions.size());
        assertEquals("account",12345L, transactions.get(0).getAccountId());
        assertEquals("category", Long.valueOf(54321L), transactions.get(0).getCategoryId());
        // assertEquals(expected, csvLine.toString().trim());
    }

    @Test
    public void readCsvAccountIdUnknown() throws CsvValidationException, IOException {
        // date,amount,note,category,account
        String csvData = "account\n" +
                "my test non existing  account";

        Name2Id<NameWithIdDto> account2Id = new Name2IdCreateIfNotExists<NameWithIdDto>(List.of(new NameWithIdDto("my test account", 12345L))) {
            @Override
            protected NameWithIdDto createItem() {
                NameWithIdDto item = new NameWithIdDto("",null);
                return item;
            }

            @Override
            protected NameWithIdDto save(NameWithIdDto newItem) {
                newItem.setId(555L);
                return newItem;
            }
        };
        Name2Id<NameWithIdDto> category2Id = new Name2Id<>(List.of(new NameWithIdDto("my test category", 54321L)));
        StringReader csvDataReader = new StringReader(csvData);

        CsvImporter importer = new CsvImporter(csvDataReader, account2Id, category2Id);

        List<Transaction> transactions = importer.readFromCsv();
        assertEquals("account",555L, transactions.get(0).getAccountId());
    }

    @Test
    public void dateString2Date() {
        String columData = "2001-02-27";
        CsvImporter importer = createImporter("");

        LocalDate locD = importer.dateString2Date(columData);
        assertEquals(columData, locD.toString());
    }

    @Test
    public void dateString2DateNullReturnsCurrentDate() {
        String columData = null;
        CsvImporter importer = createImporter("");

        LocalDate locD = importer.dateString2Date(columData);
        assertEquals(new LocalDate(), locD);
    }

    @Test
    public void floatString2Long() {
        String columData = "865.13";
        CsvImporter importer = createImporter("");

        long amount = importer.floatString2Long(columData);
        assertEquals(86513, amount);
    }
    @Test
    public void floatString2LongNull() {
        String columData = null;
        CsvImporter importer = createImporter("");

        long amount = importer.floatString2Long(columData);
        assertEquals(0l, amount);
    }

    @Test
    public void readCsvAllNull() throws CsvValidationException, IOException {
        String csvData = CsvDefinitions.COLUMN_NAME_NOTE +
                "\n\n" ;
        CsvImporter importer = createImporter(csvData);

        List<Transaction> transactions = importer.readFromCsv();
        assertNotNull("contains data", transactions);
        assertEquals("exactly one element",0, transactions.size());
    }

    @Test
    public void readCsvWithComments() throws CsvValidationException, IOException {
        String csvData = "# some comment\n"
                + CsvDefinitions.COLUMN_NAME_NOTE + "\n"
                + "\n" // empty line ignored
                + "# this is an other comment\n"
                + "My Test Transaction\n";

        CsvImporter importer = createImporter(csvData);

        Transaction transaction = importer.readFromCsv().get(0);
        assertEquals("My Test Transaction",transaction.getName());
    }


    @Test
    public void readCsvNote() throws CsvValidationException, IOException {
        String csvData = CsvDefinitions.COLUMN_NAME_NOTE +
                "\n" + "My Test Transaction";

        CsvImporter importer = createImporter(csvData);

        Transaction transaction = importer.readFromCsv().get(0);
        assertEquals("My Test Transaction",transaction.getName());
    }

    @Test
    public void readCsvAmount() throws CsvValidationException, IOException {
        String csvData = CsvDefinitions.COLUMN_NAME_AMOUNT +
                "\n" + "7.35";

        CsvImporter importer = createImporter(csvData);

        Transaction transaction = importer.readFromCsv().get(0);
        assertEquals(735, transaction.getAmount());
    }

    @Test
    public void readCsvDate() throws CsvValidationException, IOException {
        String csvData = CsvDefinitions.COLUMN_NAME_DATE +
                "\n" + "2001-02-27";

        CsvImporter importer = createImporter(csvData);
        Transaction transaction = importer.readFromCsv().get(0);

        assertEquals("2001-02-27", transaction.getDate().toString());
    }

    @Test
    public void readCsvDateWithTime() throws CsvValidationException, IOException {
        String csvData = CsvDefinitions.COLUMN_NAME_DATE +
                "\n" + "2023-01-30 09:37:05";

        CsvImporter importer = createImporter(csvData);
        Transaction transaction = importer.readFromCsv().get(0);

        assertEquals("2023-01-30", transaction.getDate().toString());
    }

    @Test
    public void testGetColumnNo_found() {
        CsvImporter importer = createImporter("");

        int columnNo = importer.getColumnNo(CsvDefinitions.CSV_HEADER_TRANSACTIONSSTRINGS, CsvDefinitions.COLUMN_NAME_NOTE);
        assertEquals(2, columnNo);

    }

    @Test
    public void testGetColumnNo_notFound() {
        CsvImporter importer = createImporter("");

        int columnNo = importer.getColumnNo(CsvDefinitions.CSV_HEADER_TRANSACTIONSSTRINGS, "doesNotExist");
        assertEquals(-1, columnNo);

    }

    @Test
    public void getColumnContent() {
        String[] data = new String[]{"zero","one"};
        CsvImporter importer = createImporter("");

        assertEquals("1", "one", importer.getColumnContent(data,1));
        assertEquals("-1",null, importer.getColumnContent(data,-1));
        assertEquals("2",null, importer.getColumnContent(data,2));
    }

    private CsvImporter createImporter(String csvData) {
        StringReader csvDataReader = new StringReader(csvData);

        return new CsvImporter(csvDataReader, null, null);
    }
}
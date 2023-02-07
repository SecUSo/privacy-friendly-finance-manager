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

import org.junit.Test;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class CsvImporterTest {
    @Test
    public void readCsv() throws CsvValidationException, IOException {
        // date,amount,note,category,account
        String csvData = "date;amount;note;category;account\n" +
                "1999-12-31;0.05;My Test Transaction;my test category;my test account";
        StringReader csvDataReader = new StringReader(csvData);

        CsvImporter importer = new CsvImporter(csvDataReader);

        List<Transaction> transactions = importer.readFromCsv();
        assertNotNull("contains data", transactions);
        assertEquals("exactly one element",1, transactions.size());
        // assertEquals(expected, csvLine.toString().trim());
    }

    @Test
    public void testGetColumnNo_found() {
        CsvImporter importer = new CsvImporter(new StringReader(""));

        int columnNo = importer.getColumnNo(CsvDefinitions.CSV_HEADER_TRANSACTIONSSTRINGS, "note");
        assertEquals(2, columnNo);

    }

    @Test
    public void testGetColumnNo_notFound() {
        CsvImporter importer = new CsvImporter(new StringReader(""));

        int columnNo = importer.getColumnNo(CsvDefinitions.CSV_HEADER_TRANSACTIONSSTRINGS, "doesNotExist");
        assertEquals(-1, columnNo);

    }
}
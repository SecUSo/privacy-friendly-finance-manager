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

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;
import org.secuso.privacyfriendlyfinance.domain.model.common.NameWithIdDto;

import java.util.Arrays;

/**
 * junit-4 test for {@link CsvExporter}
 */
public class CsvExporterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void toCsv() {
        // date,amount,note,category,account
        String expected = "1999-12-31;10000;My Test Transaction;my test category;my test account";
        Id2Name<NameWithIdDto> id2Account = new Id2Name<>(Arrays.asList(new NameWithIdDto("my test account", 12345L)));
        Id2Name<NameWithIdDto> id2Category = new Id2Name<>(Arrays.asList(new NameWithIdDto("my test category", 54321L)));

        CsvExporter exporter = new CsvExporter(id2Category, id2Account);

        Transaction transaction = new Transaction(
                "My Test Transaction",10000, LocalDate.parse("1999-12-31"),
                12345L, 54321L);

        String csvLine = exporter.toCsv(transaction);

        assertEquals(expected, csvLine);
    }

    @Test
    public void createCsvHeader() {
        String expected = "date;amount;note;category;account";

        CsvExporter exporter = new CsvExporter(null, null);

        String csvLine = exporter.createCsvHeader();

        assertEquals(expected, csvLine);
    }
}
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

import static org.junit.Assert.*;

import android.util.Log;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.secuso.privacyfriendlyfinance.domain.model.Transaction;
import org.secuso.privacyfriendlyfinance.domain.model.common.Id2Name;
import org.secuso.privacyfriendlyfinance.domain.model.common.NameWithIdDto;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * junit-4 test for {@link CsvExporter}
 */
public class CsvExporterTest {

    @Test
    public void writeCsvLine() {
        // date,amount,note,category,account
        String expected = "1999-12-31;0.05;My Test Transaction;my test category;my test account";
        Id2Name<NameWithIdDto> id2Account = new Id2Name<>(List.of(new NameWithIdDto("my test account", 12345L)));
        Id2Name<NameWithIdDto> id2Category = new Id2Name<>(List.of(new NameWithIdDto("my test category", 54321L)));

        StringWriter csvLine = new StringWriter();

        CsvExporter exporter = new CsvExporter(csvLine, id2Category, id2Account);

        Transaction transaction = new Transaction(
                "My Test Transaction",5, LocalDate.parse("1999-12-31"),
                12345L, 54321L);

        exporter.writeCsvLine(transaction);
        assertEquals(expected, csvLine.toString().trim());
    }

    @Test
    public void writeCsvHeader() {
        String expected = "date;amount;note;category;account";

        StringWriter csvLine = new StringWriter();

        CsvExporter exporter = new CsvExporter(csvLine, null, null);

        exporter.writeCsvHeader();

        assertEquals(expected, csvLine.toString().trim());
    }
}
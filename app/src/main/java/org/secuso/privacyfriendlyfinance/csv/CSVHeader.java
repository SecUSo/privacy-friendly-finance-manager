package org.secuso.privacyfriendlyfinance.csv;

import com.opencsv.bean.CsvBindByPosition;

public class CSVHeader {
    @CsvBindByPosition(position = 0)
    private Object date;
    @CsvBindByPosition(position = 1)
    private Object amount;
    @CsvBindByPosition(position = 2)
    private Object note;
    @CsvBindByPosition(position = 3)
    private Object category;
    @CsvBindByPosition(position = 4)
    private Object account;
}

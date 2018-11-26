package org.secuso.privacyfriendlyfinance.domain.convert;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class LocalDateConverter {
    @TypeConverter
    public static LocalDate fromString(String value) {
        if (value == null) {
            return null;
        } else {
            return new LocalDate(value);
        }
    }

    @TypeConverter
    public static String dateToString(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            return date.toString();
        }
    }
}

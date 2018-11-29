//package org.secuso.privacyfriendlyfinance.domain.convert;
//
//import android.arch.persistence.room.TypeConverter;
//
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormatter;
//import org.joda.time.format.ISODateTimeFormat;
//
//public class DateTimeConverter {
//    @TypeConverter
//    public static DateTime fromString(String value) {
//        if (value == null) {
//            return null;
//        } else {
//            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
//            return fmt.parseDateTime(value);
//        }
//    }
//
//    @TypeConverter
//    public static String datetimeToString(DateTime date) {
//        if (date == null) {
//            return null;
//        } else {
//            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
//            return fmt.print(date);
//        }
//    }
//}
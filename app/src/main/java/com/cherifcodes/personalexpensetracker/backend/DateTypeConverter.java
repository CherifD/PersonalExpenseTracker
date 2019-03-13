package com.cherifcodes.personalexpensetracker.backend;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTypeConverter {

    @TypeConverter
    public static LocalDate toDate(String timestamp) {
        return timestamp == null ? null : LocalDate.parse(timestamp.split("T")[0]);
    }

    @TypeConverter
    public static String toTimestamp(LocalDate date) {
        return date == null ? null : String.valueOf(date);
    }
}

package com.cherifcodes.personalexpensetracker.backend;

import android.arch.persistence.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTypeConverter {

    @TypeConverter
    public static LocalDateTime toDate(String timestamp) {
        //return timestamp == null ? null : LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofTotalSeconds(0));
        return timestamp == null ? null : LocalDateTime.parse(timestamp);
    }

    @TypeConverter
    public static String toTimestamp(LocalDateTime date) {
        //return date == null ? null : date.toInstant(ZoneOffset.ofTotalSeconds(0)).getEpochSecond();
        return date == null ? null : String.valueOf(date);
    }
}

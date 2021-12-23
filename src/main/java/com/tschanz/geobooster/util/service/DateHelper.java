package com.tschanz.geobooster.util.service;


import java.time.LocalDate;


public class DateHelper {
    public static boolean isInTimespan(LocalDate date, LocalDate timespanStart, LocalDate timespanEnd) {
        return date.isEqual(timespanStart)
            || date.isEqual(timespanEnd)
            || (date.isAfter(timespanStart) && date.isBefore(timespanEnd));
    }
}

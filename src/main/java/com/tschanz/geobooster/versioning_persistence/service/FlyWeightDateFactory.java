package com.tschanz.geobooster.versioning_persistence.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class FlyWeightDateFactory {
    private static final Map<LocalDate, LocalDate> dateMap = new HashMap<>();


    public static LocalDate get(LocalDate date) {
        var existingDate = dateMap.get(date);
        if (existingDate != null) {
            return existingDate;
        } else {
            dateMap.put(date, date);
            return date;
        }
    }
}

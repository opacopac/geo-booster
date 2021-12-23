package com.tschanz.geobooster.webmapservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class GetMapViewParams {
    private static final String SUBPARAM_SEPARATOR = ";";
    private static final String SUBPARAM_NAME_VALUE = ":";
    private static final String SUBPARAM_VALUE_QUOTES = "'";
    private static final String SUBPARAM_TYPES = "TYPEN";
    private static final String SUBPARAM_TYPES_SEPARATOR = "\\\\,";
    private static final String SUBPARAM_DATE = "DATE";
    private static final String SUBPARAM_REFRESHCOUNTER = "REFRESH_COUNTER";

    private final List<String> types;
    private final LocalDate date;
    private final long refreshCounter;


    public static GetMapViewParams parse(String value) {
        List<String> types = Collections.emptyList();
        LocalDate date = null;
        long refreshCounter = 0;

        var subparams = value.split(SUBPARAM_SEPARATOR);
        for (String subparam : subparams) {
            var name_value = subparam.split(SUBPARAM_NAME_VALUE);
            if (name_value.length != 2) {
                continue;
            }

            switch (name_value[0]) {
                case SUBPARAM_TYPES:
                    types = Arrays.asList(stripQuotes(name_value[1]).split(SUBPARAM_TYPES_SEPARATOR));
                    break;
                case SUBPARAM_DATE:
                    date = LocalDate.parse(stripQuotes(name_value[1]));
                    break;
                case SUBPARAM_REFRESHCOUNTER:
                    refreshCounter = Long.parseLong(name_value[1]);
                    break;
            }
        }

        return new GetMapViewParams(
            types,
            date,
            refreshCounter
        );
    }


    private static String stripQuotes(String value) {
        return value.replace(SUBPARAM_VALUE_QUOTES, "");
    }
}

package com.tschanz.geobooster.webmapservice.model;

import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public class GetMapRequestViewParams {
    private static final String SUBPARAM_SEPARATOR = ";";
    private static final String SUBPARAM_NAME_VALUE = ":";
    private static final String SUBPARAM_VALUE_QUOTES = "'";
    private static final String SUBPARAM_VALUE_SEPARATOR = "\\\\,";
    private static final String SUBPARAM_TYPES = "TYPEN";
    private static final String SUBPARAM_VERWALTUNGEN = "VERWALTUNGEN";
    private static final String SUBPARAM_DATE = "DATE";
    private static final String SUBPARAM_REFRESHCOUNTER = "REFRESH_COUNTER";

    private final List<VerkehrsmittelTyp> types;
    private final List<Long> verwaltungVersionIds;
    private final LocalDate date;
    private final long refreshCounter;


    public static GetMapRequestViewParams parse(String value) {
        List<VerkehrsmittelTyp> types = Collections.emptyList();
        List<Long> verwaltungIds = Collections.emptyList();
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
                    var typeNames = Arrays.asList(stripQuotes(name_value[1]).split(SUBPARAM_VALUE_SEPARATOR));
                    types = typeNames.stream().map(VerkehrsmittelTyp::valueOf).collect(Collectors.toList());
                    break;
                case SUBPARAM_VERWALTUNGEN:
                    var idStrings = Arrays.asList(name_value[1].split(SUBPARAM_VALUE_SEPARATOR));
                    verwaltungIds = idStrings.stream().map(Long::parseLong).collect(Collectors.toList());
                    break;
                case SUBPARAM_DATE:
                    date = LocalDate.parse(stripQuotes(name_value[1]));
                    break;
                case SUBPARAM_REFRESHCOUNTER:
                    refreshCounter = Long.parseLong(name_value[1]);
                    break;
            }
        }

        return new GetMapRequestViewParams(
            types,
            verwaltungIds,
            date,
            refreshCounter
        );
    }


    private static String stripQuotes(String value) {
        return value.replace(SUBPARAM_VALUE_QUOTES, "");
    }
}

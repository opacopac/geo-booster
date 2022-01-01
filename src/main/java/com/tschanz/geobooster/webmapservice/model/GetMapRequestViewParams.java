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
    private static final String SUBPARAM_LINIEN_VARIANTEN = "LINIE_VARIANTEN";
    private static final String SUBPARAM_DATE = "DATE";
    private static final String SUBPARAM_TERMINIERT = "TERMINIERT";
    private static final String SUBPARAM_REFRESHCOUNTER = "REFRESH_COUNTER";

    private final List<VerkehrsmittelTyp> types;
    private final List<Long> verwaltungVersionIds;
    private final List<Long> linienVariantenIds; // TODO: or version-ids? => please check
    private final LocalDate date;
    private final boolean showTerminiert;
    private final long refreshCounter;


    public static GetMapRequestViewParams parse(String value) {
        List<VerkehrsmittelTyp> types = Collections.emptyList();
        List<Long> verwaltungVersionIds = Collections.emptyList();
        List<Long> linienIds = Collections.emptyList();
        LocalDate date = null;
        var showTerminiert = false;
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
                    var verwaltungVersionIdStrings = Arrays.asList(name_value[1].split(SUBPARAM_VALUE_SEPARATOR));
                    verwaltungVersionIds = verwaltungVersionIdStrings.stream().map(Long::parseLong).collect(Collectors.toList());
                    break;
                case SUBPARAM_LINIEN_VARIANTEN:
                    var linienIdStrings = Arrays.asList(name_value[1].split(SUBPARAM_VALUE_SEPARATOR));
                    linienIds = linienIdStrings.stream().map(Long::parseLong).collect(Collectors.toList());
                    break;
                case SUBPARAM_DATE:
                    date = LocalDate.parse(stripQuotes(name_value[1]));
                    break;
                case SUBPARAM_TERMINIERT:
                    showTerminiert = true;
                    break;
                case SUBPARAM_REFRESHCOUNTER:
                    refreshCounter = Long.parseLong(name_value[1]);
                    break;
            }
        }

        return new GetMapRequestViewParams(
            types,
            verwaltungVersionIds,
            linienIds,
            date,
            showTerminiert,
            refreshCounter
        );
    }


    private static String stripQuotes(String value) {
        return value.replace(SUBPARAM_VALUE_QUOTES, "");
    }
}

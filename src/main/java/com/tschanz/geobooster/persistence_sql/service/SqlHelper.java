package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlDialect;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SqlHelper {
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_WITHOUT_NANO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    public static String getToDate(SqlDialect sqlDialect, LocalDate date) {
        switch (sqlDialect) {
            case MYSQL:
                return String.format("STR_TO_DATE('%s', '%%Y-%%m-%%d')", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            case ORACLE:
                return String.format("TO_DATE('%s', 'YYYY-MM-DD')", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            default:
                throw new IllegalArgumentException("Unknown SQL dialect " + sqlDialect.name());
        }
    }


    public static String getToDate(SqlDialect sqlDialect, LocalDateTime dateTime) {
        switch (sqlDialect) {
            case MYSQL:
                return String.format("STR_TO_DATE('%s', '%%Y-%%m-%%dT%%H:%%i:%%s')", dateTime.format(ISO_LOCAL_DATE_TIME_WITHOUT_NANO));
            case ORACLE:
                return String.format("TO_DATE('%s', 'YYYY-MM-DD\"T\"HH24:MI:SS')", dateTime.format(ISO_LOCAL_DATE_TIME_WITHOUT_NANO));
            default:
                throw new IllegalArgumentException("Unknown SQL dialect " + sqlDialect.name());
        }
    }


    public static LocalDate parseLocalDatefromJsonAgg(String jsonAggDate) {
        return LocalDate.parse(jsonAggDate, ISO_LOCAL_DATE_TIME_WITHOUT_NANO);
    }
}

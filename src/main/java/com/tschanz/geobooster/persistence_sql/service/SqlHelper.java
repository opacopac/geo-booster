package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.SqlDialect;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class SqlHelper {
    public static String getToDate(SqlDialect sqlDialect, LocalDate date) {
        switch (sqlDialect) {
            case MYSQL:
                return String.format("STR_TO_DATE('%s', '%%Y-%%m-%%d')", date.format(DateTimeFormatter.ISO_DATE));
            case ORACLE:
                return String.format("TO_DATE('%s', 'YYYY-MM-DD')", date.format(DateTimeFormatter.ISO_DATE));
            default:
                throw new IllegalArgumentException("Unknown SQL dialect " + sqlDialect.name());
        }
    }
}

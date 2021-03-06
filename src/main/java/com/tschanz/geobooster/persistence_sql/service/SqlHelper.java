package com.tschanz.geobooster.persistence_sql.service;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import lombok.SneakyThrows;

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


    @SneakyThrows
    public static String parseStringOrNullfromJsonAgg(JsonReader reader) {
        var nextToken = reader.peek();
        if (JsonToken.NULL.equals(nextToken)) {
            reader.nextNull();
            return null;
        } else {
            return reader.nextString();
        }
    }


    @SneakyThrows
    public static LocalDate parseLocalDateOrNullfromJsonAgg(JsonReader reader) {
        var nextToken = reader.peek();
        if (JsonToken.NULL.equals(nextToken)) {
            reader.nextNull();
            return null;
        } else {
            return LocalDate.parse(reader.nextString(), ISO_LOCAL_DATE_TIME_WITHOUT_NANO);
        }
    }


    @SneakyThrows
    public static long parseLongOrDefaultFromJsonAgg(JsonReader reader, long defaultValue) {
        var nextToken = reader.peek();
        if (JsonToken.NULL.equals(nextToken)) {
            reader.nextNull();
            return defaultValue;
        } else {
            return reader.nextLong();
        }
    }
}

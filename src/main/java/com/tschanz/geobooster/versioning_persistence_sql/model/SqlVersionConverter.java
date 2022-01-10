package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.versioning_persistence.service.FlyWeightDateFactory;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;


public class SqlVersionConverter {
    public final static String COL_IDELEMENT = "ID_ELEMENT";
    public final static String COL_GUELTIGVON = "GUELTIG_VON";
    public final static String COL_GUELTIGBIS = "GUELTIG_BIS";
    public final static String[] SELECT_COLS = {SqlHasIdConverter.COL_ID, COL_IDELEMENT, COL_GUELTIGVON, COL_GUELTIGBIS};


    @SneakyThrows
    public static long getElementId(ResultSet row) {
        return row.getLong(COL_IDELEMENT);
    }


    @SneakyThrows
    public static LocalDate getGueltigVon(ResultSet row) {
        return FlyWeightDateFactory.get(row.getDate(COL_GUELTIGVON).toLocalDate());
    }


    @SneakyThrows
    public static LocalDate getGueltigBis(ResultSet row) {
        return FlyWeightDateFactory.get(row.getDate(COL_GUELTIGBIS).toLocalDate());
    }


    @SneakyThrows
    public static long getElementIdFromJsonAgg(JsonReader reader) {
        return reader.nextLong();
    }


    @SneakyThrows
    public static LocalDate getGueltigVonFromJsonAgg(JsonReader reader) {
        return LocalDate.parse(reader.nextString());
    }


    @SneakyThrows
    public static LocalDate getGueltigBisFromJsonAgg(JsonReader reader) {
        return LocalDate.parse(reader.nextString());
    }
}

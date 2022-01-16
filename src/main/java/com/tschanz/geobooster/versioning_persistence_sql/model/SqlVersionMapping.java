package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.service.FlyWeightDateFactory;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;


public class SqlVersionMapping {
    public final static String COL_IDELEMENT = "ID_ELEMENT";
    public final static String COL_GUELTIGVON = "GUELTIG_VON";
    public final static String COL_GUELTIGBIS = "GUELTIG_BIS";
    public final static String COL_TERMINIERT_PER = "TERMINIERT_PER";
    public final static String[] SELECT_COLS = {SqlHasIdMapping.COL_ID, COL_IDELEMENT, COL_GUELTIGVON, COL_GUELTIGBIS};
    public final static String[] SELECT_COLS_W_TERM_PER = ArrayHelper.appendTo(SELECT_COLS, COL_TERMINIERT_PER);


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
    public static LocalDate getTerminiertPer(ResultSet row) {
        var terminiertPer = row.getDate(COL_TERMINIERT_PER);
        if (terminiertPer != null) {
            return FlyWeightDateFactory.get(terminiertPer.toLocalDate());
        } else {
            return null;
        }
    }


    @SneakyThrows
    public static long getElementIdFromJsonAgg(JsonReader reader) {
        return reader.nextLong();
    }


    @SneakyThrows
    public static LocalDate getGueltigVonFromJsonAgg(JsonReader reader) {
        return SqlHelper.parseLocalDateOrNullfromJsonAgg(reader);
    }


    @SneakyThrows
    public static LocalDate getGueltigBisFromJsonAgg(JsonReader reader) {
        return SqlHelper.parseLocalDateOrNullfromJsonAgg(reader);
    }
}

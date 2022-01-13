package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlTarifkanteVersionConverter implements SqlResultsetConverter<TarifkanteVersion>, SqlJsonAggConverter<TarifkanteVersion> {
    public final static String TABLE_NAME = "N_TARIFKANTE_V";

    private final Collection<Long> versionIds;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionConverter.SELECT_COLS_W_TERM_PER;
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s %s",
            String.join(",", this.getSelectFields()),
            this.getTable(),
            !this.versionIds.isEmpty() ? this.getWhereClause(versionIds) : ""
        );
    }


    @SneakyThrows
    public TarifkanteVersion fromResultSet(ResultSet row) {
        return new TarifkanteVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            SqlVersionConverter.getTerminiertPer(row),
            Collections.emptyList()
        );
    }



    @Override
    public TarifkanteVersion fromJsonAgg(JsonReader reader) {
        return new TarifkanteVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader),
            SqlHelper.parseLocalDateOrNullfromJsonAgg(reader),
            Collections.emptyList()
        );
    }


    private String getWhereClause(Collection<Long> versionIds) {
        var idStrings = versionIds.stream().map(Object::toString).collect(Collectors.toList());
        return String.format(
            " WHERE(%s IN (%s)",
            SqlHasIdConverter.COL_ID,
            String.join(",", idStrings)
        );
    }
}

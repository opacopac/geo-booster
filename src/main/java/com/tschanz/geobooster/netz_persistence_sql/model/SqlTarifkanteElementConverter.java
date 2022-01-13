package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlTarifkanteElementConverter implements SqlResultsetConverter<Tarifkante>, SqlJsonAggConverter<Tarifkante> {
    private final static String COL_HST1 = "ID_HS_ELEMENT_1";
    private final static String COL_HST2 = "ID_HS_ELEMENT_2";

    private final Collection<Long> elementIds;


    @Override
    public String getTable() {
        return "N_TARIFKANTE_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[]{
            SqlHasIdConverter.COL_ID,
            COL_HST1,
            COL_HST2
        };
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s %s",
            String.join(",", this.getSelectFields()),
            this.getTable(),
            !this.elementIds.isEmpty() ? this.getWhereClause(elementIds) : ""
        );
    }


    @SneakyThrows
    public Tarifkante fromResultSet(ResultSet row) {
        return new Tarifkante(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_HST1),
            row.getLong(COL_HST2)
        );
    }


    @Override
    @SneakyThrows
    public Tarifkante fromJsonAgg(JsonReader reader) {
        return new Tarifkante(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong()
        );
    }


    private String getWhereClause(Collection<Long> elementIds) {
        var idStrings = elementIds.stream().map(Object::toString).collect(Collectors.toList());
        return String.format(
            " WHERE(%s IN (%s)",
            SqlHasIdConverter.COL_ID,
            String.join(",", idStrings)
        );
    }
}

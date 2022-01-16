package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;


@RequiredArgsConstructor
public class SqlTarifkanteElementMapping implements SqlStandardMapping<Tarifkante, SqlLongFilter, Long> {
    private final static String COL_HST1 = "ID_HS_ELEMENT_1";
    private final static String COL_HST2 = "ID_HS_ELEMENT_2";

    private final Collection<Long> elementIds;


    @Override
    public String getTable() {
        return "N_TARIFKANTE_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[]{ SqlHasIdMapping.COL_ID, COL_HST1, COL_HST2};
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.elementIds);
    }


    @SneakyThrows
    public Tarifkante fromResultSet(ResultSet row) {
        return new Tarifkante(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_HST1),
            row.getLong(COL_HST2)
        );
    }


    @Override
    @SneakyThrows
    public Tarifkante fromJsonAgg(JsonReader reader) {
        return new Tarifkante(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong()
        );
    }
}

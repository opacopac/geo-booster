package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlTarifkanteVersionMapping implements SqlStandardMapping<TarifkanteVersion, SqlLongFilter, Long> {
    public final static String TABLE_NAME = "N_TARIFKANTE_V";

    private final Collection<Long> versionIds;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionMapping.SELECT_COLS_W_TERM_PER;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.versionIds);
    }


    @SneakyThrows
    public TarifkanteVersion mapRow(ResultSet row, int rowNum) {
        return new TarifkanteVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            SqlVersionMapping.getTerminiertPer(row),
            Pflegestatus.PRODUKTIV,
            Collections.emptyList()
        );
    }


    @Override
    public TarifkanteVersion fromJsonAgg(JsonReader reader) {
        return new TarifkanteVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            SqlHelper.parseLocalDateOrNullfromJsonAgg(reader),
            Pflegestatus.PRODUKTIV,
            Collections.emptyList()
        );
    }
}

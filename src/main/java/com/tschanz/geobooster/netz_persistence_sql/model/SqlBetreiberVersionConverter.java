package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlBetreiberVersionConverter implements SqlStandardConverter<BetreiberVersion, SqlLongFilter, Long> {
    @Override
    public String getTable() {
        return "N_BETREIBER_V";
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionConverter.SELECT_COLS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public BetreiberVersion fromResultSet(ResultSet row) {
        return new BetreiberVersion(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    public BetreiberVersion fromJsonAgg(JsonReader reader) {
        return new BetreiberVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }
}

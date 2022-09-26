package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


@RequiredArgsConstructor
public class SqlAwbVersionMapping implements SqlStandardMapping<AwbVersion, SqlLongFilter, Long> {
    public final static String TABLE_NAME = "T_ANWBER_V";

    private final Collection<Long> versionIds;
    private final Map<Long, Collection<Long>> includeVkaMap;
    private final Map<Long, Collection<Long>> excludeVkaMap;
    private final Map<Long, Collection<Long>> includeTkMap;
    private final Map<Long, Collection<Long>> excludeTkMap;
    private final Map<Long, Collection<Long>> includeZpMap;
    private final Map<Long, Collection<Long>> includeRgaMap;


    @Override
    public String getTable() {
        return TABLE_NAME;
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionMapping.SELECT_COLS_W_PFLEGESTATUS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return SqlLongFilter.createSingleton(SqlHasIdMapping.COL_ID, this.versionIds);
    }


    @Override
    @SneakyThrows
    public AwbVersion fromResultSet(ResultSet row) {
        return this.createAwbVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            SqlVersionMapping.getPflegestatus(row)
        );
    }


    @Override
    public AwbVersion fromJsonAgg(JsonReader reader) {
        return this.createAwbVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            SqlVersionMapping.getPflegestatusFromJsonAgg(reader)
        );
    }


    private AwbVersion createAwbVersion(long id, long elementId, LocalDate gueltigVon, LocalDate gueltigBis, Pflegestatus pflegestatus) {
        return new AwbVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            pflegestatus,
            this.includeVkaMap.getOrDefault(id, Collections.emptyList()),
            this.excludeVkaMap.getOrDefault(id, Collections.emptyList()),
            this.includeTkMap.getOrDefault(id, Collections.emptyList()),
            this.excludeTkMap.getOrDefault(id, Collections.emptyList()),
            this.includeZpMap.getOrDefault(id, Collections.emptyList()),
            this.includeRgaMap.getOrDefault(id, Collections.emptyList())
        );
    }
}

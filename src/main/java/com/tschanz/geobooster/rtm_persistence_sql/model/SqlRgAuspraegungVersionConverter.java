package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardConverter;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlRgAuspraegungVersionConverter implements SqlStandardConverter<RgAuspraegungVersion, SqlLongFilter, Long> {
    private final Map<Long, RgAuspraegung> rgAuspraegungMap;
    private final Map<Long, Collection<RgKorridor>> rgKorridorByRgMap;
    private final Map<Long, Collection<RgKorridorVersion>> rgKorridorVersionMap;
    private final Map<Long, Collection<Long>> rgKorrTkIds;


    @Override
    public String getTable() {
        return "R_RG_AUSPRAEGUNG_V";
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionConverter.SELECT_COLS;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    @Override
    public RgAuspraegungVersion fromResultSet(ResultSet row) {
        return this.createRga(
            SqlHasIdConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row)
        );
    }


    @Override
    public RgAuspraegungVersion fromJsonAgg(JsonReader reader) {
        return this.createRga(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader)
        );
    }


    private RgAuspraegungVersion createRga(
        long id,
        long elementId,
        LocalDate gueltigVon,
        LocalDate gueltigBis
    ) {
        var rga = this.rgAuspraegungMap.get(elementId);
        var korrEs = this.rgKorridorByRgMap.get(rga.getRelationsgebietId());

        Collection<Long> tkIds = korrEs == null ? Collections.emptyList() : korrEs.stream()
            .map(korrE -> {
                var korrVs = this.rgKorridorVersionMap.get(korrE.getId());
                return korrVs != null ? VersioningHelper.filterSingleVersion(korrVs, gueltigBis) : null;
            })
            .filter(Objects::nonNull)
            .map(korrV -> this.rgKorrTkIds.get(korrV.getId()))
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());

        return new RgAuspraegungVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            tkIds
        );
    }
}

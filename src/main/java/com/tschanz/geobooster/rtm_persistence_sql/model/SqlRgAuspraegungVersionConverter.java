package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlRgAuspraegungVersionConverter implements SqlResultsetConverter<RgAuspraegungVersion> {
    private final Map<Long, RgAuspraegung> rgAuspraegungMap;
    private final Map<Long, Collection<RgKorridor>> rgKorridorByRgMap;
    private final Map<Long, Collection<RgKorridorVersion>> rgKorridorVersionMap;
    private final Map<Long, Collection<Long>> rgKorrTkIds;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM R_RG_AUSPRAEGUNG_V",
            String.join(",", SqlVersionConverter.SELECT_COLS)
        );
    }


    @SneakyThrows
    @Override
    public RgAuspraegungVersion fromResultSet(ResultSet row) {
        var gueltigBis = SqlVersionConverter.getGueltigBis(row);
        var rgaEId = SqlVersionConverter.getElementId(row);
        var rga = this.rgAuspraegungMap.get(rgaEId);
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
            SqlHasIdConverter.getId(row),
            rgaEId,
            SqlVersionConverter.getGueltigVon(row),
            gueltigBis,
            tkIds
        );
    }
}

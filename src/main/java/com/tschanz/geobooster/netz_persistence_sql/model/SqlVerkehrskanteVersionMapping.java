package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlVerkehrskanteVersionMapping implements SqlStandardMapping<VerkehrskanteVersion, SqlLongFilter, Long> {
    public final Map<Long, Collection<VerkehrskanteAuspraegung>> vkVkasMap;
    public final Map<Long, Collection<VerkehrskanteAuspraegungVersion>> vkaVersionMap;


    @Override
    public String getTable() {
        return "N_VERKEHRSKANTE_V";
    }


    @Override
    public String[] getSelectFields() {
        return SqlVersionMapping.SELECT_COLS_W_TERM_PER;
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @Override
    @SneakyThrows
    public VerkehrskanteVersion fromResultSet(ResultSet row) {
        var vkEId = SqlVersionMapping.getElementId(row);
        var gueltigVon = SqlVersionMapping.getGueltigVon(row);
        var gueltigBis = SqlVersionMapping.getGueltigBis(row);

        return new VerkehrskanteVersion(
            SqlHasIdMapping.getId(row),
            vkEId,
            gueltigVon,
            gueltigBis,
            SqlVersionMapping.getTerminiertPer(row),
            this.getVerwaltungIds(vkEId, gueltigBis),
            this.getVmBitmask(vkEId, gueltigBis)
        );
    }


    @Override
    @SneakyThrows
    public VerkehrskanteVersion fromJsonAgg(JsonReader reader) {
        var id = SqlHasIdMapping.getIdFromJsonAgg(reader);
        var elementId = SqlVersionMapping.getElementIdFromJsonAgg(reader);
        var gueltigVon = SqlVersionMapping.getGueltigVonFromJsonAgg(reader);
        var gueltigBis = SqlVersionMapping.getGueltigBisFromJsonAgg(reader);

        return new VerkehrskanteVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            SqlHelper.parseLocalDateOrNullfromJsonAgg(reader),
            this.getVerwaltungIds(elementId, gueltigBis),
            this.getVmBitmask(elementId, gueltigBis)
        );
    }



    private Collection<Long> getVerwaltungIds(long vkEId, LocalDate date) {
        return this.vkVkasMap.get(vkEId).stream()
            .filter(vka -> this.hasValidVkaVersion(vka.getId(), date))
            .map(VerkehrskanteAuspraegung::getVerwaltungId)
            .collect(Collectors.toList());
    }


    private byte getVmBitmask(long vkEId, LocalDate date) {
        var vmTypes = this.vkVkasMap.get(vkEId).stream()
            .filter(vka -> this.hasValidVkaVersion(vka.getId(), date))
            .map(VerkehrskanteAuspraegung::getVerkehrsmittelTyp)
            .collect(Collectors.toList());

        return VerkehrsmittelTyp.getBitMask(vmTypes);
    }


    private boolean hasValidVkaVersion(long vkaId, LocalDate date) {
        var vkaVersions = this.vkaVersionMap.get(vkaId);
        if (vkaVersions == null) {
            return false;
        }

        return vkaVersions.stream()
            .anyMatch(vkaV -> VersioningHelper.isVersionInTimespan(vkaV, date));
    }
}

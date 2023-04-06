package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
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
    public VerkehrskanteVersion mapRow(ResultSet row, int rowNum) {
        return this.createVerkehrskanteVersion(
            SqlHasIdMapping.getId(row),
            SqlVersionMapping.getElementId(row),
            SqlVersionMapping.getGueltigVon(row),
            SqlVersionMapping.getGueltigBis(row),
            SqlVersionMapping.getTerminiertPer(row)
        );
    }


    @Override
    @SneakyThrows
    public VerkehrskanteVersion fromJsonAgg(JsonReader reader) {
        return this.createVerkehrskanteVersion(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            SqlVersionMapping.getElementIdFromJsonAgg(reader),
            SqlVersionMapping.getGueltigVonFromJsonAgg(reader),
            SqlVersionMapping.getGueltigBisFromJsonAgg(reader),
            SqlHelper.parseLocalDateOrNullfromJsonAgg(reader)
        );
    }


    private VerkehrskanteVersion createVerkehrskanteVersion(
        long id,
        long elementId,
        LocalDate gueltigVon,
        LocalDate gueltigBis,
        LocalDate terminiertPer
    ) {
        return new VerkehrskanteVersion(
            id,
            elementId,
            gueltigVon,
            gueltigBis,
            terminiertPer,
            Pflegestatus.PRODUKTIV,
            this.getVerwaltungIds(elementId, gueltigBis),
            this.getVmBitmask(elementId, gueltigBis)
        );
    }


    private Collection<Long> getVerwaltungIds(long vkEId, LocalDate date) {
        var vkas = this.vkVkasMap.get(vkEId);
        if (vkas == null) {
            return Collections.emptyList();
        }

        return vkas
            .stream()
            .filter(vka -> this.hasValidVkaVersion(vka.getId(), date))
            .map(VerkehrskanteAuspraegung::getVerwaltungId)
            .collect(Collectors.toList());
    }


    private byte getVmBitmask(long vkEId, LocalDate date) {
        var vkas = this.vkVkasMap.get(vkEId);
        if (vkas == null) {
            return VerkehrsmittelTyp.getBitMask(Collections.emptyList());
        }

        var vmTypes = vkas
            .stream()
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

        return vkaVersions
            .stream()
            .anyMatch(vkaV -> VersioningHelper.isVersionInTimespan(vkaV, date));
    }
}

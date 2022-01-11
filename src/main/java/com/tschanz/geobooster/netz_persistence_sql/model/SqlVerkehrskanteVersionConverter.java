package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlVerkehrskanteVersionConverter implements SqlResultsetConverter<VerkehrskanteVersion>, SqlJsonAggConverter<VerkehrskanteVersion> {
    public final Map<Long, Collection<VerkehrskanteAuspraegung>> vkVkasMap;
    public final Map<Long, Collection<VerkehrskanteAuspraegungVersion>> vkaVersionMap;


    @Override
    public String getTable() {
        return "N_VERKEHRSKANTE_V";
    }


    @Override
    public String[] getFields() {
        return SqlVersionConverter.SELECT_COLS_W_TERM_PER;
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s",
            String.join(",", this.getFields()),
            this.getTable()
        );
    }


    @Override
    @SneakyThrows
    public VerkehrskanteVersion fromResultSet(ResultSet row) {
        var vkEId = SqlVersionConverter.getElementId(row);
        var gueltigVon = SqlVersionConverter.getGueltigVon(row);
        var gueltigBis = SqlVersionConverter.getGueltigBis(row);

        return new VerkehrskanteVersion(
            SqlHasIdConverter.getId(row),
            vkEId,
            gueltigVon,
            gueltigBis,
            SqlVersionConverter.getTerminiertPer(row),
            this.getVerwaltungIds(vkEId, gueltigBis),
            this.getVmBitmask(vkEId, gueltigBis)
        );
    }


    @Override
    @SneakyThrows
    public VerkehrskanteVersion fromJsonAgg(JsonReader reader) {
        var id = SqlHasIdConverter.getIdFromJsonAgg(reader);
        var elementId = SqlVersionConverter.getElementIdFromJsonAgg(reader);
        var gueltigVon = SqlVersionConverter.getGueltigVonFromJsonAgg(reader);
        var gueltigBis = SqlVersionConverter.getGueltigBisFromJsonAgg(reader);

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

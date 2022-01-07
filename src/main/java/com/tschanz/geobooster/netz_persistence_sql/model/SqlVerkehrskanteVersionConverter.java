package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.service.FlyWeightDateFactory;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlVerkehrskanteVersionConverter implements SqlResultsetConverter<VerkehrskanteVersion> {
    private final static String COL_TERMINIERT_PER = "TERMINIERT_PER";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_TERMINIERT_PER);

    public final Map<Long, List<VerkehrskanteAuspraegung>> vkVkasMap;
    public final Map<Long, List<VerkehrskanteAuspraegungVersion>> vkaVersionMap;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_VERKEHRSKANTE_V",
            String.join(",", SELECT_COLS)
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
            this.getTerminiertPer(row),
            this.getVerwaltungIds(vkEId, gueltigBis),
            this.getVmBitmask(vkEId, gueltigBis)
        );
    }


    private List<Long> getVerwaltungIds(long vkEId, LocalDate date) {
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


    @SneakyThrows
    private LocalDate getTerminiertPer(ResultSet row) {
        var terminiertPer = row.getDate(COL_TERMINIERT_PER);
        if (terminiertPer != null) {
            return FlyWeightDateFactory.get(terminiertPer.toLocalDate());
        } else {
            return null;
        }
    }


    private boolean hasValidVkaVersion(long vkaId, LocalDate date) {
        var vkaVersions = this.vkaVersionMap.get(vkaId);
        if (vkaVersions == null) {
            return false;
        }

        return vkaVersions.stream()
            .anyMatch(vkaV -> (date.isAfter(vkaV.getGueltigVon()) || date.isEqual(vkaV.getGueltigVon()))
                && (date.isBefore(vkaV.getGueltigBis()) || date.isEqual(vkaV.getGueltigBis())));
    }
}

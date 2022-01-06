package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteAuspraegungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteAuspraegungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class VerkehrskanteAuspraegungSqlPersistence implements VerkehrskanteAuspraegungPersistence {
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegung> readAllElements() {
        var query = String.format(
            "SELECT %s FROM N_VERKEHRS_KANTE_AUSPR_E",
            String.join(",", SqlVerkehrskanteAuspraegungElementConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlVerkehrskanteAuspraegungElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegungVersion> readAllVersions() {
        var query = String.format(
            "SELECT %s FROM N_VERKEHRS_KANTE_AUSPR_V",
            String.join(",", SqlVerkehrskanteAuspraegungVersionConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlVerkehrskanteAuspraegungVersionConverter());
    }
}

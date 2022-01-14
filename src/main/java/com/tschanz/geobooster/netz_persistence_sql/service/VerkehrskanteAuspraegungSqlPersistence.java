package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVkAuspraegungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVkAuspraegungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class VerkehrskanteAuspraegungSqlPersistence implements VerkehrskanteAuspraegungPersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegung> readAllElements() {
        var converter = new SqlVkAuspraegungElementConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegungVersion> readAllVersions() {
        var converter = new SqlVkAuspraegungVersionConverter();

        return this.sqlReader.read(converter);
    }
}

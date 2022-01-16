package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVkAuspraegungElementMapping;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVkAuspraegungVersionMapping;
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
        var mapping = new SqlVkAuspraegungElementMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegungVersion> readAllVersions() {
        var mapping = new SqlVkAuspraegungVersionMapping();

        return this.sqlReader.read(mapping);
    }
}

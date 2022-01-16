package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementMapping;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class VerwaltungSqlPersistence implements VerwaltungPersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Verwaltung> readAllElements() {
        var mapping = new SqlVerwaltungElementMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    @SneakyThrows
    public Collection<VerwaltungVersion> readAllVersions() {
        var mapping = new SqlVerwaltungVersionMapping();

        return this.sqlReader.read(mapping);
    }
}

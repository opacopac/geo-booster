package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionConverter;
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
        var converter = new SqlVerwaltungElementConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    @SneakyThrows
    public Collection<VerwaltungVersion> readAllVersions() {
        var converter = new SqlVerwaltungVersionConverter();

        return this.sqlReader.read(converter);
    }
}

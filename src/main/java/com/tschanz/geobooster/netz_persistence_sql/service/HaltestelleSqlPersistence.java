package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleElementMapping;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleVersionMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class HaltestelleSqlPersistence implements HaltestellenPersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Haltestelle> readAllElements() {
        var mapping = new SqlHaltestelleElementMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    @SneakyThrows
    public Collection<HaltestelleVersion> readAllVersions() {
        var mapping = new SqlHaltestelleVersionMapping();

        return this.sqlReader.read(mapping);
    }
}

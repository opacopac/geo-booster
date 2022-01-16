package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_persistence.service.HaltestelleWegangabePersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlHaltestelleWegangabeElementMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlHaltestelleWegangabeVersionMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class HaltestelleWegangabeSqlPersistence implements HaltestelleWegangabePersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<HaltestelleWegangabe> readAllElements() {
        var mapping = new SqlHaltestelleWegangabeElementMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    @SneakyThrows
    public Collection<HaltestelleWegangabeVersion> readAllVersions() {
        var mapping = new SqlHaltestelleWegangabeVersionMapping();

        return this.sqlReader.read(mapping);
    }
}

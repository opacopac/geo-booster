package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabe;
import com.tschanz.geobooster.rtm.model.HaltestelleWegangabeVersion;
import com.tschanz.geobooster.rtm_persistence.service.HaltestelleWegangabePersistence;
import com.tschanz.geobooster.rtm_persistence.service.SqlHaltestelleWegangabeVersionConverter;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlHaltestelleWegangabeElementConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class HaltestelleWegangabeSqlPersistence implements HaltestelleWegangabePersistence {
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<HaltestelleWegangabe> readAllElements() {
        var converter = new SqlHaltestelleWegangabeElementConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    @SneakyThrows
    public Collection<HaltestelleWegangabeVersion> readAllVersions() {
        return this.sqlReader.read(new SqlHaltestelleWegangabeVersionConverter());
    }
}

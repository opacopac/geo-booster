package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgKorridorPersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorElementMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorTkIdsMapping;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorVersionMapping;
import com.tschanz.geobooster.util.model.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class RgKorridorSqlPersistence implements RgKorridorPersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<RgKorridor> readAllElements() {
        var mapping = new SqlRgKorridorElementMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    @SneakyThrows
    public Collection<RgKorridorVersion> readAllVersions() {
        var mapping = new SqlRgKorridorVersionMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllKorridorTkIds() {
        var mapping = new SqlRgKorridorTkIdsMapping();

        return this.sqlReader.read(mapping);
    }
}

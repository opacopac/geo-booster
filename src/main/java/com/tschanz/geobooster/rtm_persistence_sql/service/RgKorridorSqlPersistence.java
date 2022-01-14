package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.rtm_persistence.service.RgKorridorPersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorElementConverter;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorTkIdsConverter;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgKorridorVersionConverter;
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
        var converter = new SqlRgKorridorElementConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    @SneakyThrows
    public Collection<RgKorridorVersion> readAllVersions() {
        var converter = new SqlRgKorridorVersionConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllKorridorTkIds() {
        var converter = new SqlRgKorridorTkIdsConverter();

        return this.sqlReader.read(converter);
    }
}

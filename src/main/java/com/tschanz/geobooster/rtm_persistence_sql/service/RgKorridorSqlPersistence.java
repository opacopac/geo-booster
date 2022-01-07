package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
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
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<RgKorridor> readAllElements() {
        return this.sqlReader.read(new SqlRgKorridorElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<RgKorridorVersion> readAllVersions() {
        return this.sqlReader.read(new SqlRgKorridorVersionConverter());
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllKorridorTkIds() {
        return this.sqlReader.read(new SqlRgKorridorTkIdsConverter());
    }
}

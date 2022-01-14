package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneElementConverter;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVersionConverter;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVkIdsConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class ZoneSqlPersistence implements ZonePersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Zone> readAllElements() {
        var converter = new SqlZoneElementConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    @SneakyThrows
    public Collection<ZoneVersion> readAllVersions() {
        var converter = new SqlZoneVersionConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllVkIds() {
        var converter = new SqlZoneVkIdsConverter();

        return this.sqlReader.read(converter);
    }
}

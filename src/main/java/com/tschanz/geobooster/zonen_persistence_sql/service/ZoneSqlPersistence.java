package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneElementMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVersionMapping;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZoneVkIdsMapping;
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
        var mapping = new SqlZoneElementMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    @SneakyThrows
    public Collection<ZoneVersion> readAllVersions() {
        var mapping = new SqlZoneVersionMapping();

        return this.sqlReader.read(mapping);
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllVkIds() {
        var mapping = new SqlZoneVkIdsMapping();

        return this.sqlReader.read(mapping);
    }
}

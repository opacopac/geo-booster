package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
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
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Zone> readAllElements() {
        return this.sqlReader.read(new SqlZoneElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<ZoneVersion> readAllVersions() {
        return this.sqlReader.read(new SqlZoneVersionConverter());
    }


    @Override
    public Collection<KeyValue<Long, Long>> readAllVkIds() {
        return this.sqlReader.read(new SqlZoneVkIdsConverter());
    }
}

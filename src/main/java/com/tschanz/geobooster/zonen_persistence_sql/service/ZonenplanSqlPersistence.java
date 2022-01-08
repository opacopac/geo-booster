package com.tschanz.geobooster.zonen_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.Zonenplan;
import com.tschanz.geobooster.zonen.model.ZonenplanVersion;
import com.tschanz.geobooster.zonen_persistence.service.ZonePersistence;
import com.tschanz.geobooster.zonen_persistence.service.ZonenplanPersistence;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZonenplanElementConverter;
import com.tschanz.geobooster.zonen_persistence_sql.model.SqlZonenplanVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class ZonenplanSqlPersistence implements ZonenplanPersistence {
    private final SqlReader sqlReader;
    private final ZonePersistence zonePersistence;


    @Override
    @SneakyThrows
    public Collection<Zonenplan> readAllElements() {
        return this.sqlReader.read(new SqlZonenplanElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<ZonenplanVersion> readAllVersions() {
        var rgaEs = this.readAllElements();

        return this.readAllVersions(rgaEs);
    }


    @Override
    public Collection<ZonenplanVersion> readAllVersions(Collection<Zonenplan> elements) {
        var zpEMap = VersioningHelper.createIdMap(elements);

        var zoneEs = this.zonePersistence.readAllElements();
        var zoneEByZpMap = ArrayHelper.create1toNLookupMap(zoneEs, Zone::getZonenplanId, k -> k);

        var zoneVs = this.zonePersistence.readAllVersions();
        var zoneByEVMap = VersioningHelper.createElementIdMap(zoneVs);

        var zoneVkIds = this.zonePersistence.readAllVkIds();
        var zoneVkIdsMap = ArrayHelper.create1toNLookupMap(zoneVkIds, KeyValue::getKey, KeyValue::getValue);

        // TODO
        Map<Long, Long> excludeVks = Collections.emptyMap();

        var converter = new SqlZonenplanVersionConverter(zpEMap, zoneEByZpMap, zoneByEVMap, zoneVkIdsMap, excludeVks);

        return this.sqlReader.read(converter);
    }
}

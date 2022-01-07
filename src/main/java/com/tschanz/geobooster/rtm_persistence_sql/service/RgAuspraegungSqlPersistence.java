package com.tschanz.geobooster.rtm_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.rtm.model.RgAuspraegungVersion;
import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm_persistence.service.RgAuspraegungPersistence;
import com.tschanz.geobooster.rtm_persistence.service.RgKorridorPersistence;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgAuspraegungElementConverter;
import com.tschanz.geobooster.rtm_persistence_sql.model.SqlRgAuspraegungVersionConverter;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.VersioningHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class RgAuspraegungSqlPersistence implements RgAuspraegungPersistence {
    private final SqlReader sqlReader;
    private final RgKorridorPersistence rgKorridorPersistence;


    @Override
    @SneakyThrows
    public Collection<RgAuspraegung> readAllElements() {
        return this.sqlReader.read(new SqlRgAuspraegungElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<RgAuspraegungVersion> readAllVersions() {
        var rgaEs = this.readAllElements();

        return this.readAllVersions(rgaEs);
    }


    @Override
    public Collection<RgAuspraegungVersion> readAllVersions(Collection<RgAuspraegung> elements) {
        var rgaEMap = VersioningHelper.createIdMap(elements);

        var rgKorrEs = this.rgKorridorPersistence.readAllElements();
        var rgKorrEByRgMap = ArrayHelper.create1toNLookupMap(rgKorrEs, RgKorridor::getRelationsgbietId, k -> k);

        var rgKorrVs = this.rgKorridorPersistence.readAllVersions();
        var rgKorrVMap = VersioningHelper.createElementIdMap(rgKorrVs);

        var rgKorrTkIds = this.rgKorridorPersistence.readAllKorridorTkIds();
        var rgKorrTkIdsMap = ArrayHelper.create1toNLookupMap(rgKorrTkIds, KeyValue::getKey, KeyValue::getValue);

        var converter = new SqlRgAuspraegungVersionConverter(rgaEMap, rgKorrEByRgMap, rgKorrVMap, rgKorrTkIdsMap);

        return this.sqlReader.read(converter);
    }
}

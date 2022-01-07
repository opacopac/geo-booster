package com.tschanz.geobooster.tarif_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.service.AwbPersistence;
import com.tschanz.geobooster.tarif_persistence_sql.model.*;
import com.tschanz.geobooster.util.service.ArrayHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AwbSqlPersistence implements AwbPersistence {
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Awb> readAllElements() {
        return this.sqlReader.read(new SqlAwbElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<AwbVersion> readAllVersions() {
        var includeVkMap = this.readIncludeVkMap();
        var excludeVkMap = this.readExcludeVkMap();
        var includeTkMap = this.readIncludeTkMap();
        var excludeTkMap = this.readExcludeTkMap();
        var includeVerwMap = this.readIncludeVerwMap();
        var includeZpMap = this.readIncludeZpMap();
        var includeRgaMap = this.readIncludeRgaMap();
        var converter = new SqlAwbVersionConverter(
            includeVkMap,
            excludeVkMap,
            includeTkMap,
            excludeTkMap,
            includeVerwMap,
            includeZpMap,
            includeRgaMap
        );

        return this.sqlReader.read(converter);
    }


    private Map<Long, Collection<Long>> readIncludeVkMap() {
        var excludeVks = this.sqlReader.read(new SqlAwbIncVkConverter());

        return ArrayHelper.create1toNLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readExcludeVkMap() {
        var excludeVks = this.sqlReader.read(new SqlAwbExcVkConverter());

        return ArrayHelper.create1toNLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeTkMap() {
        var excludeTks = this.sqlReader.read(new SqlAwbIncTkConverter());

        return ArrayHelper.create1toNLookupMap(excludeTks);
    }


    private Map<Long, Collection<Long>> readExcludeTkMap() {
        var excludeTks = this.sqlReader.read(new SqlAwbExcTkConverter());

        return ArrayHelper.create1toNLookupMap(excludeTks);
    }


    private Map<Long, Collection<Long>> readIncludeVerwMap() {
        var excludeVks = this.sqlReader.read(new SqlAwbIncVerwConverter());

        return ArrayHelper.create1toNLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeZpMap() {
        var excludeVks = this.sqlReader.read(new SqlAwbIncZpConverter());

        return ArrayHelper.create1toNLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeRgaMap() {
        var excludeVks = sqlReader.read(new SqlAwbIncRgaConverter());

        return ArrayHelper.create1toNLookupMap(excludeVks);
    }
}

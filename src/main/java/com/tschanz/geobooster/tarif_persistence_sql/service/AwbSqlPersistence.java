package com.tschanz.geobooster.tarif_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.tarif.model.AwbVersion;
import com.tschanz.geobooster.tarif_persistence.AwbPersistence;
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
        var query = String.format(
            "SELECT %s FROM T_ANWBER_E",
            String.join(",", SqlAwbElementConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlAwbElementConverter());
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

        var query = String.format(
            "SELECT %s FROM T_ANWBER_V",
            String.join(",", SqlAwbVersionConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, converter);
    }


    private Map<Long, Collection<Long>> readIncludeVkMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_INCLUDE_KANTEN",
            String.join(",", SqlAwbIncVkConverter.SELECT_COLS)
        );
        var excludeVks = this.sqlReader.read(query, new SqlAwbIncVkConverter());

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readExcludeVkMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_EXCLUDE_KANTEN",
            String.join(",", SqlAwbExcVkConverter.SELECT_COLS)
        );
        var excludeVks = this.sqlReader.read(query, new SqlAwbExcVkConverter());

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeTkMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_IN_TARIF_KANTEN",
            String.join(",", SqlAwbIncTkConverter.SELECT_COLS)
        );

        var excludeTks = this.sqlReader.read(query, new SqlAwbIncTkConverter());

        return ArrayHelper.createLookupMap(excludeTks);
    }


    private Map<Long, Collection<Long>> readExcludeTkMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_EX_TARIF_KANTEN",
            String.join(",", SqlAwbExcTkConverter.SELECT_COLS)
        );

        var excludeTks = this.sqlReader.read(query, new SqlAwbExcTkConverter());

        return ArrayHelper.createLookupMap(excludeTks);
    }


    private Map<Long, Collection<Long>> readIncludeVerwMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_INCLUDE_VERWALTUNG",
            String.join(",", SqlAwbIncVerwConverter.SELECT_COLS)
        );
        var excludeVks = this.sqlReader.read(query, new SqlAwbIncVerwConverter());

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeZpMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_INCLUDE_ZONENPLAN",
            String.join(",", SqlAwbIncZpConverter.SELECT_COLS)
        );
        var excludeVks = this.sqlReader.read(query, new SqlAwbIncZpConverter());

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeRgaMap() {
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_RG_AUSPRAEGUNG",
            String.join(",", SqlAwbIncRgaConverter.SELECT_COLS)
        );
        var excludeVks = sqlReader.read(query, new SqlAwbIncRgaConverter());

        return ArrayHelper.createLookupMap(excludeVks);
    }
}

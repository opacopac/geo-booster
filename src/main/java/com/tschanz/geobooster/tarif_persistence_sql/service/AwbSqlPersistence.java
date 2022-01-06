package com.tschanz.geobooster.tarif_persistence_sql.service;

import com.tschanz.geobooster.netz_persistence_sql.service.SqlReader;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
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
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Awb> readAllElements() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbElementConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_E",
            String.join(",", SqlAwbElementConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
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

        var sqlReader = new SqlReader<>(this.connectionFactory, converter);
        var query = String.format(
            "SELECT %s FROM T_ANWBER_V",
            String.join(",", SqlAwbVersionConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
    }


    private Map<Long, Collection<Long>> readIncludeVkMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbIncVkConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_INCLUDE_KANTEN",
            String.join(",", SqlAwbIncVkConverter.SELECT_COLS)
        );
        var excludeVks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readExcludeVkMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbExcVkConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_EXCLUDE_KANTEN",
            String.join(",", SqlAwbExcVkConverter.SELECT_COLS)
        );
        var excludeVks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeTkMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbIncTkConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_IN_TARIF_KANTEN",
            String.join(",", SqlAwbIncTkConverter.SELECT_COLS)
        );

        var excludeTks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeTks);
    }


    private Map<Long, Collection<Long>> readExcludeTkMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbExcTkConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_EX_TARIF_KANTEN",
            String.join(",", SqlAwbExcTkConverter.SELECT_COLS)
        );

        var excludeTks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeTks);
    }


    private Map<Long, Collection<Long>> readIncludeVerwMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbIncVerwConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_INCLUDE_VERWALTUNG",
            String.join(",", SqlAwbIncVerwConverter.SELECT_COLS)
        );
        var excludeVks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeZpMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbIncZpConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_INCLUDE_ZONENPLAN",
            String.join(",", SqlAwbIncZpConverter.SELECT_COLS)
        );
        var excludeVks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeVks);
    }


    private Map<Long, Collection<Long>> readIncludeRgaMap() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlAwbIncRgaConverter());
        var query = String.format(
            "SELECT %s FROM T_ANWBER_X_RG_AUSPRAEGUNG",
            String.join(",", SqlAwbIncRgaConverter.SELECT_COLS)
        );
        var excludeVks = sqlReader.read(query);

        return ArrayHelper.createLookupMap(excludeVks);
    }
}

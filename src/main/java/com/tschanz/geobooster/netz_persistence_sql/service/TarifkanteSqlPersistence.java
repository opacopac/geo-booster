package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.netz_persistence.service.TarifkantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.*;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.service.SqlJsonAggReader;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import com.tschanz.geobooster.util.model.KeyValue;
import com.tschanz.geobooster.util.service.ArrayHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class TarifkanteSqlPersistence implements TarifkantePersistence {
    private final ConnectionState connectionState;
    private final SqlJsonAggReader jsonAggReader;
    private final SqlReader sqlReader;


    @Override
    public Collection<Tarifkante> readAllElements() {
        return this.readElements(null);
    }


    @Override
    @SneakyThrows
    public Collection<Tarifkante> readElements(ReadFilter filter) {
        var converter = new SqlTarifkanteElementConverter(filter, this.connectionState.getSqlDialect());
        if (this.connectionState.isUseJsonAgg() && filter == null) {
            return this.jsonAggReader.read(converter);
        } else {
            return this.sqlReader.read(converter);
        }
    }


    @Override
    public Collection<TarifkanteVersion> readAllVersions() {
        return this.readVersions(null);
    }


    @Override
    @SneakyThrows
    public Collection<TarifkanteVersion> readVersions(ReadFilter filter) {
        List<TarifkanteVersion> tkVs;
        var converter = new SqlTarifkanteVersionConverter(filter, this.connectionState.getSqlDialect());
        if (this.connectionState.isUseJsonAgg() && filter == null) {
            tkVs = this.jsonAggReader.read(converter);
        } else {
            tkVs = this.sqlReader.read(converter);
        }

        if (tkVs.isEmpty()) {
            return tkVs; // no need to load tkvks
        }

        // add linked vks
        List<Long> onlyTkVIds = filter != null
            ? tkVs.stream().map(TarifkanteVersion::getId).collect(Collectors.toList())
            : null;
        var tkVkMap = this.readTkVkMap(onlyTkVIds);
        tkVs.forEach(tkV -> {
            var vkIds = tkVkMap.get(tkV.getId());
            if (vkIds != null) {
                tkV.setVerkehrskanteIds(vkIds);
            }
        });

        return tkVs;
    }


    @Override
    @SneakyThrows
    public long readVersionCount() {
        return this.sqlReader.read(new SqlVerkehrskanteCountConverter()).get(0);
    }


    @Override
    @SneakyThrows
    public Collection<Long> readAllVersionIds() {
        if (this.connectionState.isUseJsonAgg()) {
            return this.sqlReader.read(new SqlVerkehrskanteVersionIdJsonAggConverter()).get(0);
        } else {
            return this.sqlReader.read(new SqlVerkehrskanteVersionIdConverter());
        }
    }


    @SneakyThrows
    private Map<Long, Collection<Long>> readTkVkMap(List<Long> filterTkVIds) {
        var converter = new SqlTkVkConverter(filterTkVIds);
        var tkVkList = this.sqlReader.read(converter);

        return ArrayHelper.create1toNLookupMap(tkVkList, KeyValue::getKey, KeyValue::getValue);
    }
}

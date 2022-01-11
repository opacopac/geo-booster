package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz_persistence.service.HaltestellenPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleVersionConverter;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.service.SqlJsonAggReader;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class HaltestelleSqlPersistence implements HaltestellenPersistence {
    private final ConnectionState connectionState;
    private final SqlJsonAggReader jsonAggReader;
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Haltestelle> readAllElements() {
        var converter = new SqlHaltestelleElementConverter();
        if (this.connectionState.isUseJsonAgg()) {
            return this.jsonAggReader.read(converter);
        } else {
            return this.sqlReader.read(converter);
        }
    }


    @Override
    @SneakyThrows
    public Collection<HaltestelleVersion> readAllVersions() {
        var converter = new SqlHaltestelleVersionConverter();
        if (this.connectionState.isUseJsonAgg()) {
            return this.jsonAggReader.read(converter);
        } else {
            return this.sqlReader.read(converter);
        }
    }
}

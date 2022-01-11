package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberVersionConverter;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.service.SqlJsonAggReader;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class BetreiberSqlPersistence implements BetreiberPersistence {
    private final ConnectionState connectionState;
    private final SqlJsonAggReader jsonAggReader;
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Betreiber> readAllElements() {
        var converter = new SqlBetreiberElementConverter();
        if (this.connectionState.isUseJsonAgg()) {
            return this.jsonAggReader.read(converter);
        } else {
            return this.sqlReader.read(converter);
        }
    }


    @Override
    @SneakyThrows
    public Collection<BetreiberVersion> readAllVersions() {
        var converter = new SqlBetreiberVersionConverter();
        if (this.connectionState.isUseJsonAgg()) {
            return this.jsonAggReader.read(converter);
        } else {
            return this.sqlReader.read(converter);
        }
    }
}

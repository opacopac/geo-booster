package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class BetreiberSqlPersistence implements BetreiberPersistence {
    private final SqlStandardReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Betreiber> readAllElements() {
        var converter = new SqlBetreiberElementConverter();

        return this.sqlReader.read(converter);
    }


    @Override
    @SneakyThrows
    public Collection<BetreiberVersion> readAllVersions() {
        var converter = new SqlBetreiberVersionConverter();

        return this.sqlReader.read(converter);
    }
}

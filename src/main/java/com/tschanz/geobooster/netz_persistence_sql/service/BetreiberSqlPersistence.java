package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class BetreiberSqlPersistence implements BetreiberPersistence {
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Betreiber> readAllElements() {
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_E",
            String.join(",", SqlBetreiberElementConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlBetreiberElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<BetreiberVersion> readAllVersions() {
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_V",
            String.join(",", SqlBetreiberVersionConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlBetreiberVersionConverter());
    }
}

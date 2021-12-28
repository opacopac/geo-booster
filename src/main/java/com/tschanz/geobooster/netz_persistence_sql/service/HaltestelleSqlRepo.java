package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Haltestelle;
import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.service.HaltestellenPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlHaltestelleVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class HaltestelleSqlRepo implements HaltestellenPersistenceRepo {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Haltestelle> readAllElements() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlHaltestelleElementConverter(),
            "%d hst elements loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_HALTESTELLE_E",
            String.join(",", SqlHaltestelleElementConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<HaltestelleVersion> readAllVersions() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlHaltestelleVersionConverter(),
            "%d hst versions loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_HALTESTELLE_V",
            String.join(",", SqlHaltestelleVersionConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }
}

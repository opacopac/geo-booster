package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz.service.VerwaltungPersistenceRepo;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class VerwaltungSqlRepo implements VerwaltungPersistenceRepo {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Verwaltung> readAllElements() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlVerwaltungElementConverter(),
            "%d verwaltung elements loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_E",
            String.join(",", SqlVerwaltungElementConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<VerwaltungVersion> readAllVersions() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlVerwaltungVersionConverter(),
            "%d verwaltung versions loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_V",
            String.join(",", SqlVerwaltungVersionConverter.ALL_COLS)
        );

        return sqlReader.read(query);
    }
}

package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class VerwaltungSqlPersistence implements VerwaltungPersistence {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Verwaltung> readAllElements() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlVerwaltungElementConverter());
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_E",
            String.join(",", SqlVerwaltungElementConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<VerwaltungVersion> readAllVersions() {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlVerwaltungVersionConverter());
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_V",
            String.join(",", SqlVerwaltungVersionConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
    }
}

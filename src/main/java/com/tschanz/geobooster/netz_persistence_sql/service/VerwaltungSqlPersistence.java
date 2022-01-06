package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.netz.model.VerwaltungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerwaltungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerwaltungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class VerwaltungSqlPersistence implements VerwaltungPersistence {
    private final SqlReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Verwaltung> readAllElements() {
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_E",
            String.join(",", SqlVerwaltungElementConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlVerwaltungElementConverter());
    }


    @Override
    @SneakyThrows
    public Collection<VerwaltungVersion> readAllVersions() {
        var query = String.format(
            "SELECT %s FROM N_VERWALTUNG_V",
            String.join(",", SqlVerwaltungVersionConverter.SELECT_COLS)
        );

        return this.sqlReader.read(query, new SqlVerwaltungVersionConverter());
    }
}

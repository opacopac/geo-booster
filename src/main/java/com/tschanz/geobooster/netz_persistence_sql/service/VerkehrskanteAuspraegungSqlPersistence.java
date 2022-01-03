package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegungVersion;
import com.tschanz.geobooster.netz_persistence.service.VerkehrskanteAuspraegungPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteAuspraegungElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVerkehrskanteAuspraegungVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class VerkehrskanteAuspraegungSqlPersistence implements VerkehrskanteAuspraegungPersistence {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegung> readAllElements() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlVerkehrskanteAuspraegungElementConverter(),
            "%d vka elements loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_VERKEHRS_KANTE_AUSPR_E",
            String.join(",", SqlVerkehrskanteAuspraegungElementConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<VerkehrskanteAuspraegungVersion> readAllVersions() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlVerkehrskanteAuspraegungVersionConverter(),
            "%d vka versions loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_VERKEHRS_KANTE_AUSPR_V",
            String.join(",", SqlVerkehrskanteAuspraegungVersionConverter.SELECT_COLS)
        );


        return sqlReader.read(query);
    }
}

package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.netz_persistence.service.BetreiberPersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberElementConverter;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlBetreiberVersionConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class BetreiberSql implements BetreiberPersistence {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Betreiber> readAllElements() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlBetreiberElementConverter(),
            "%d betreiber elements loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_E",
            String.join(",", SqlBetreiberElementConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
    }


    @Override
    @SneakyThrows
    public Collection<BetreiberVersion> readAllVersions() {
        var sqlReader = new SqlReader<>(
            this.connectionFactory,
            new SqlBetreiberVersionConverter(),
            "%d betreiber versions loaded...",
            2
        );
        var query = String.format(
            "SELECT %s FROM N_BETREIBER_V",
            String.join(",", SqlBetreiberVersionConverter.SELECT_COLS)
        );

        return sqlReader.read(query);
    }
}

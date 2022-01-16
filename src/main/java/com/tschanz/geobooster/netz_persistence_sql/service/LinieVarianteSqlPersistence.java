package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz_persistence.service.LinieVariantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlLinVarTkMapping;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlLinVarVkMapping;
import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.service.SqlGenericResultsetReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class LinieVarianteSqlPersistence implements LinieVariantePersistence {
    private final ConnectionState connectionState;
    private final SqlGenericResultsetReader sqlReader;


    @Override
    @SneakyThrows
    public Collection<Long> searchVerkehrskanteIds(Collection<Long> linieVarianteIds) {
        var mapping = new SqlLinVarVkMapping(linieVarianteIds);

        return this.sqlReader.read(mapping);
    }


    @Override
    public Collection<Long> searchTarifkanteIds(Collection<Long> linieVarianteIds, LocalDate date) {
        var mapping = new SqlLinVarTkMapping(
            linieVarianteIds,
            date,
            this.connectionState.getSqlDialect()
        );

        return this.sqlReader.read(mapping);
    }
}

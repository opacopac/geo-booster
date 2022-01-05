package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz_persistence.service.LinieVariantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlVkIdsByLinVarIdsConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LinieVarianteSqlPersistence implements LinieVariantePersistence {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Long> searchVerkehrskantenIdsByLinienVarianteIds(Collection<Long> linieVarianteIds) {
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlVkIdsByLinVarIdsConverter());
        var query = String.format(
            "SELECT vk_e.ID as %s FROM N_VERKEHRSKANTE_E vk_e"
            + " INNER JOIN N_VERKEHRS_KANTE_AUSPR_E vka_e ON vka_e.ID_VERKEHRSKANTE_E = vk_e.ID"
            + " INNER JOIN N_LINIE_VARIANTE_KANTEN lvk ON lvk.ID_KANTEN_AUSPRAEGUNG_E = vka_e.ID"
            + " WHERE lvk.ID_LINIE_VARIANTE IN (%s)",
            SqlVkIdsByLinVarIdsConverter.COL_ID,
            linieVarianteIds.stream().map(Object::toString).collect(Collectors.joining(","))
        );

        return sqlReader.read(query);
    }
}

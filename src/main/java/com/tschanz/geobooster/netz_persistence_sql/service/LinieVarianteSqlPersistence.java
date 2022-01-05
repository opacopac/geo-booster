package com.tschanz.geobooster.netz_persistence_sql.service;

import com.tschanz.geobooster.netz_persistence.service.LinieVariantePersistence;
import com.tschanz.geobooster.netz_persistence_sql.model.SqlDynamicIdConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlConnectionFactory;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LinieVarianteSqlPersistence implements LinieVariantePersistence {
    private final SqlConnectionFactory connectionFactory;


    @Override
    @SneakyThrows
    public Collection<Long> searchVerkehrskanteIds(Collection<Long> linieVarianteIds) {
        var idColName = "VK_ID";
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlDynamicIdConverter(idColName));
        var query = String.format(
            "SELECT vk_e.ID as %s FROM N_VERKEHRSKANTE_E vk_e"
            + " INNER JOIN N_VERKEHRS_KANTE_AUSPR_E vka_e ON vka_e.ID_VERKEHRSKANTE_E = vk_e.ID"
            + " INNER JOIN N_LINIE_VARIANTE_KANTEN lvk ON lvk.ID_KANTEN_AUSPRAEGUNG_E = vka_e.ID"
            + " WHERE lvk.ID_LINIE_VARIANTE IN (%s)",
            idColName,
            linieVarianteIds.stream().map(Object::toString).collect(Collectors.joining(","))
        );

        return sqlReader.read(query);
    }


    @Override
    public Collection<Long> searchTarifkanteIds(Collection<Long> linieVarianteIds, LocalDate date) {
        var idColName = "TK_ID";
        var sqlReader = new SqlReader<>(this.connectionFactory, new SqlDynamicIdConverter(idColName));
        var dateString = SqlHelper.getToDate(this.connectionFactory.getSqlDialect(), date);
        var query = String.format(
            "SELECT tk_e.ID as %s FROM N_TARIFKANTE_E tk_e"
                + " INNER JOIN N_TARIFKANTE_V tk_v ON tk_v.ID_ELEMENT = tk_e.ID"
                + " INNER JOIN N_TARIFKANTE_X_N_VERK_KANTE_E tk_vk ON tk_vk.ID_TARIFKANTE_V = tk_v.ID"
                + " INNER JOIN N_VERKEHRSKANTE_E vk_e ON vk_e.ID = tk_vk.ID_VERKEHRS_KANTE_E"
                + " INNER JOIN N_VERKEHRS_KANTE_AUSPR_E vka_e ON vka_e.ID_VERKEHRSKANTE_E = vk_e.ID"
                + " INNER JOIN N_LINIE_VARIANTE_KANTEN lvk ON lvk.ID_KANTEN_AUSPRAEGUNG_E = vka_e.ID"
                + " WHERE lvk.ID_LINIE_VARIANTE IN (%s) AND %s >= tk_v.GUELTIG_VON AND %s <= tk_v.GUELTIG_BIS",
            idColName,
            linieVarianteIds.stream().map(Object::toString).collect(Collectors.joining(",")),
            dateString,
            dateString
        );

        return sqlReader.read(query);
    }
}

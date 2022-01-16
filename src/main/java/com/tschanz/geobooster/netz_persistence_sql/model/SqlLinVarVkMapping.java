package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlGenericResultsetMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlLinVarVkMapping implements SqlGenericResultsetMapping<Long> {
    private final String COL_ID = "VK_ID";
    private final Collection<Long> linieVarianteIds;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT vk_e.ID as %s FROM N_VERKEHRSKANTE_E vk_e"
                + " INNER JOIN N_VERKEHRS_KANTE_AUSPR_E vka_e ON vka_e.ID_VERKEHRSKANTE_E = vk_e.ID"
                + " INNER JOIN N_LINIE_VARIANTE_KANTEN lvk ON lvk.ID_KANTEN_AUSPRAEGUNG_E = vka_e.ID"
                + " WHERE lvk.ID_LINIE_VARIANTE IN (%s)",
            COL_ID,
            this.linieVarianteIds.stream().map(Object::toString).collect(Collectors.joining(","))
        );
    }


    @SneakyThrows
    @Override
    public Long fromResultSet(ResultSet row) {
        return row.getLong(COL_ID);
    }
}

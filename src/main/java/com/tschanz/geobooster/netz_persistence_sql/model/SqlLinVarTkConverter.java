package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlLinVarTkConverter implements SqlResultsetConverter<Long> {
    private final String COL_ID = "TK_ID";
    private final Collection<Long> linieVarianteIds;
    private final LocalDate date;
    private final SqlDialect sqlDialect;


    @Override
    public String getSelectQuery() {
        var dateString = SqlHelper.getToDate(this.sqlDialect, this.date);
        return String.format(
            "SELECT tk_e.ID as %s FROM N_TARIFKANTE_E tk_e"
                + " INNER JOIN N_TARIFKANTE_V tk_v ON tk_v.ID_ELEMENT = tk_e.ID"
                + " INNER JOIN N_TARIFKANTE_X_N_VERK_KANTE_E tk_vk ON tk_vk.ID_TARIFKANTE_V = tk_v.ID"
                + " INNER JOIN N_VERKEHRSKANTE_E vk_e ON vk_e.ID = tk_vk.ID_VERKEHRS_KANTE_E"
                + " INNER JOIN N_VERKEHRS_KANTE_AUSPR_E vka_e ON vka_e.ID_VERKEHRSKANTE_E = vk_e.ID"
                + " INNER JOIN N_LINIE_VARIANTE_KANTEN lvk ON lvk.ID_KANTEN_AUSPRAEGUNG_E = vka_e.ID"
                + " WHERE lvk.ID_LINIE_VARIANTE IN (%s) AND %s >= tk_v.GUELTIG_VON AND %s <= tk_v.GUELTIG_BIS",
            COL_ID,
            linieVarianteIds.stream().map(Object::toString).collect(Collectors.joining(",")),
            dateString,
            dateString
        );
    }


    @SneakyThrows
    @Override
    public Long fromResultSet(ResultSet row) {
        return row.getLong(COL_ID);
    }
}

package com.tschanz.geobooster.rtm_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.rtm.model.RgAuspraegung;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;


@RequiredArgsConstructor
public class SqlRgAuspraegungElementConverter implements SqlResultsetConverter<RgAuspraegung> {
    private final static String COL_ID_RELATIONSGEBIET_E = "ID_RELATIONSGEBIET_E";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_ID_RELATIONSGEBIET_E);


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM R_RG_AUSPRAEGUNG_E",
            String.join(",", SELECT_COLS)
        );
    }


    @Override
    @SneakyThrows
    public RgAuspraegung fromResultSet(ResultSet row) {
        return new RgAuspraegung(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_ID_RELATIONSGEBIET_E)
        );
    }
}

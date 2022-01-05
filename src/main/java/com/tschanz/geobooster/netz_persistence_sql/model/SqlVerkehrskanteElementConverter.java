package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerkehrskanteElementConverter implements SqlResultsetConverter<Verkehrskante> {
    public final static String COL_HST1 = "ID_HS_ELEMENT_1";
    public final static String COL_HST2 = "ID_HS_ELEMENT_2";
    public final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_HST1, COL_HST2);


    @SneakyThrows
    @Override
    public Verkehrskante fromResultSet(ResultSet row) {
        return new Verkehrskante(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_HST1),
            row.getLong(COL_HST2)
        );
    }
}

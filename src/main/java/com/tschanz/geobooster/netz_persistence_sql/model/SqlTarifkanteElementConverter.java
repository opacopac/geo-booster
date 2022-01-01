package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlTarifkanteElementConverter implements SqlResultsetConverter<Tarifkante> {
    public final static String COL_HST1 = "ID_HS_ELEMENT_1";
    public final static String COL_HST2 = "ID_HS_ELEMENT_2";
    public final static String COL_CREATED_AT = "CREATED_AT";
    public final static String COL_MODIFIED_AT = "MODIFIED_AT";
    public final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_HST1, COL_HST2);


    @SneakyThrows
    public Tarifkante fromResultSet(ResultSet row) {
        return new Tarifkante(
            SqlElementConverter.getId(row),
            row.getLong(COL_HST1),
            row.getLong(COL_HST2)
        );
    }
}

package com.tschanz.geobooster.tarif_persistence_sql.model;

import com.tschanz.geobooster.netz_persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.tarif.model.Awb;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlAwbElementConverter implements SqlResultsetConverter<Awb> {
    public final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS);


    @Override
    @SneakyThrows
    public Awb fromResultSet(ResultSet row) {
        return new Awb(
            SqlHasIdConverter.getId(row)
        );
    }
}

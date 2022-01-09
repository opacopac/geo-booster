package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.Gson;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;


public class SqlVerkehrskanteVersionIdOracleConverter implements SqlResultsetConverter<List<Long>> {
    static private final String COL_TKV_IDS = "TKV_IDS";

    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT JSON_ARRAYAGG(%s RETURNING CLOB) AS %s FROM N_TARIFKANTE_V",
            SqlHasIdConverter.COL_ID,
            COL_TKV_IDS
        );
    }


    @SneakyThrows
    @Override
    public List<Long> fromResultSet(ResultSet row) {
        var gson = new Gson();
        var tkIdsClob = row.getClob(COL_TKV_IDS);
        var tkIdArray = gson.fromJson(tkIdsClob.getCharacterStream(), Long[].class);

        return Arrays.asList(tkIdArray);
    }
}

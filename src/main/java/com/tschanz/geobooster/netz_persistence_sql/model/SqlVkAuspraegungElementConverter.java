package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVkAuspraegungElementConverter implements SqlResultsetConverter<VerkehrskanteAuspraegung>, SqlJsonAggConverter<VerkehrskanteAuspraegung> {
    private final static String COL_AUSPTYP = "KANTE_AUSPRAEGUNG_TYP";
    private final static String COL_VK_E_ID = "ID_VERKEHRSKANTE_E";
    private final static String COL_VERW_E_ID = "ID_VERWALTUNG_E";


    @Override
    public String getTable() {
        return "N_VERKEHRS_KANTE_AUSPR_E";
    }


    @Override
    public String[] getFields() {
        return ArrayHelper.appendTo(SqlElementConverter.SELECT_COLS, COL_AUSPTYP, COL_VK_E_ID, COL_VERW_E_ID);
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s",
            String.join(",", this.getFields()),
            this.getTable()
        );
    }


    @SneakyThrows
    @Override
    public VerkehrskanteAuspraegung fromResultSet(ResultSet row) {
        return new VerkehrskanteAuspraegung(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_VK_E_ID),
            row.getLong(COL_VERW_E_ID),
            VerkehrsmittelTyp.valueOf(row.getString(COL_AUSPTYP))
        );
    }



    @Override
    @SneakyThrows
    public VerkehrskanteAuspraegung fromJsonAgg(JsonReader reader) {
        return new VerkehrskanteAuspraegung(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong(),
            VerkehrsmittelTyp.valueOf(reader.nextString())
        );
    }
}

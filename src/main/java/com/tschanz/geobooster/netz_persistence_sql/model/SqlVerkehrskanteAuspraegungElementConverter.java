package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;


public class SqlVerkehrskanteAuspraegungElementConverter implements SqlResultsetConverter<VerkehrskanteAuspraegung> {
    public final static String COL_ID = "ID";
    public final static String COL_AUSPTYP = "KANTE_AUSPRAEGUNG_TYP";
    public final static String COL_VK_E_ID = "ID_VERKEHRSKANTE_E";
    public final static String COL_VERW_E_ID = "ID_VERWALTUNG_E";
    public final static String[] SELECT_COLS = {COL_ID, COL_AUSPTYP, COL_VK_E_ID, COL_VERW_E_ID};


    @SneakyThrows
    @Override
    public VerkehrskanteAuspraegung fromResultSet(ResultSet row) {
        return new VerkehrskanteAuspraegung(
            SqlElementConverter.getId(row),
            row.getLong(COL_VK_E_ID),
            row.getLong(COL_VERW_E_ID),
            VerkehrsmittelTyp.valueOf(row.getString(COL_AUSPTYP))
        );
    }
}

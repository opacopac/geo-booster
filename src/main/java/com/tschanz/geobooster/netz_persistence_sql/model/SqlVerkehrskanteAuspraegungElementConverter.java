package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.Verkehrskante;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.netz.model.Verwaltung;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlElementInfoConverter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Map;


public class SqlVerkehrskanteAuspraegungElementConverter {
    public final static String COL_ID = "ID";
    public final static String COL_AUSPTYP = "KANTE_AUSPRAEGUNG_TYP";
    public final static String COL_VK_E_ID = "ID_VERKEHRSKANTE_E";
    public final static String COL_VERW_E_ID = "ID_VERWALTUNG_E";
    public final static String[] ALL_COLS = {COL_ID, COL_AUSPTYP, COL_VK_E_ID, COL_VERW_E_ID};


    @SneakyThrows
    public static VerkehrskanteAuspraegung fromResultSet(ResultSet row, Map<Long, Verkehrskante> verkehrskanteMap, Map<Long, Verwaltung> verwaltungMap) {
        return new VerkehrskanteAuspraegung(
            SqlElementInfoConverter.fromResultSet(row),
            verkehrskanteMap.get(row.getLong(COL_VK_E_ID)),
            verwaltungMap.get(row.getLong(COL_VERW_E_ID)),
            VerkehrsmittelTyp.valueOf(row.getString(COL_AUSPTYP))
        );
    }
}

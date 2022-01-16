package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.VerkehrskanteAuspraegung;
import com.tschanz.geobooster.netz.model.VerkehrsmittelTyp;
import com.tschanz.geobooster.persistence_sql.model.SqlLongFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdMapping;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;


public class SqlVkAuspraegungElementMapping implements SqlStandardMapping<VerkehrskanteAuspraegung, SqlLongFilter, Long> {
    private final static String COL_VK_E_ID = "ID_VERKEHRSKANTE_E";
    private final static String COL_VERW_E_ID = "ID_VERWALTUNG_E";
    private final static String COL_AUSPTYP = "KANTE_AUSPRAEGUNG_TYP";


    @Override
    public String getTable() {
        return "N_VERKEHRS_KANTE_AUSPR_E";
    }


    @Override
    public String[] getSelectFields() {
        return new String[] {
            SqlHasIdMapping.COL_ID,
            COL_VK_E_ID,
            COL_VERW_E_ID,
            COL_AUSPTYP
        };
    }


    @Override
    public Collection<SqlLongFilter> getFilters() {
        return Collections.emptyList();
    }


    @SneakyThrows
    @Override
    public VerkehrskanteAuspraegung fromResultSet(ResultSet row) {
        return new VerkehrskanteAuspraegung(
            SqlHasIdMapping.getId(row),
            row.getLong(COL_VK_E_ID),
            row.getLong(COL_VERW_E_ID),
            VerkehrsmittelTyp.valueOf(row.getString(COL_AUSPTYP))
        );
    }



    @Override
    @SneakyThrows
    public VerkehrskanteAuspraegung fromJsonAgg(JsonReader reader) {
        return new VerkehrskanteAuspraegung(
            SqlHasIdMapping.getIdFromJsonAgg(reader),
            reader.nextLong(),
            SqlHelper.parseLongOrDefaultFromJsonAgg(reader, 0),
            VerkehrsmittelTyp.valueOf(reader.nextString())
        );
    }
}

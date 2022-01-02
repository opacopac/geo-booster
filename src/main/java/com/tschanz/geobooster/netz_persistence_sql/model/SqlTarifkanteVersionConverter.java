package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.service.FlyWeightDateFactory;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collections;


@RequiredArgsConstructor
public class SqlTarifkanteVersionConverter implements SqlResultsetConverter<TarifkanteVersion> {
    public final static String COL_TERMINIERT_PER = "TERMINIERT_PER";
    public final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_TERMINIERT_PER);


    @SneakyThrows
    public TarifkanteVersion fromResultSet(ResultSet row) {
        return new TarifkanteVersion(
            SqlVersionConverter.getId(row),
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            this.getTerminiertPer(row),
            Collections.emptyList()
        );
    }


    @SneakyThrows
    private LocalDate getTerminiertPer(ResultSet row) {
        var terminiertPer = row.getDate(COL_TERMINIERT_PER);
        if (terminiertPer != null) {
            return FlyWeightDateFactory.get(terminiertPer.toLocalDate());
        } else {
            return null;
        }
    }
}

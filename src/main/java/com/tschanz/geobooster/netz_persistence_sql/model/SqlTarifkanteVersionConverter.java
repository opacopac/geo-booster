package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning.service.FlyWeightDateFactory;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class SqlTarifkanteVersionConverter implements SqlResultsetConverter<TarifkanteVersion> {
    public final static String COL_TERMINIERT_PER = "TERMINIERT_PER";
    public final static String[] ALL_COLS = ArrayHelper.appendTo(SqlVersionConverter.ALL_COLS, COL_TERMINIERT_PER);

    public final Map<Long, List<Long>> tkVkMap;


    @SneakyThrows
    public TarifkanteVersion fromResultSet(ResultSet row) {
        var id = SqlVersionConverter.getId(row);
        var vkIds = tkVkMap.get(id);
        if (vkIds == null) {
            vkIds = Collections.emptyList();
        }

        return new TarifkanteVersion(
            id,
            SqlVersionConverter.getElementId(row),
            SqlVersionConverter.getGueltigVon(row),
            SqlVersionConverter.getGueltigBis(row),
            this.getTerminiertPer(row),
            vkIds
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

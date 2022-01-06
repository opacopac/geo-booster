package com.tschanz.geobooster.netz_persistence_sql.model;

import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.util.service.ArrayHelper;
import com.tschanz.geobooster.versioning_persistence.service.FlyWeightDateFactory;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlTarifkanteVersionConverter implements SqlResultsetConverter<TarifkanteVersion> {
    private final static String COL_TERMINIERT_PER = "TERMINIERT_PER";
    private final static String COL_CREATED_AT = "CREATED_AT";
    private final static String COL_MODIFIED_AT = "MODIFIED_AT";
    private final static String[] SELECT_COLS = ArrayHelper.appendTo(SqlVersionConverter.SELECT_COLS, COL_TERMINIERT_PER);

    private final ReadFilter readFilter;
    private final SqlDialect sqlDialect;


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM N_TARIFKANTE_V %s",
            String.join(",", SqlTarifkanteVersionConverter.SELECT_COLS),
            this.readFilter != null ? this.getWhereClauseForFilter(readFilter) : ""
        );
    }


    @SneakyThrows
    public TarifkanteVersion fromResultSet(ResultSet row) {
        return new TarifkanteVersion(
            SqlHasIdConverter.getId(row),
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


    private String getWhereClauseForFilter(ReadFilter filter) {
        List<String> conditions = new ArrayList<>();

        if (filter.getIdList() != null) {
            var idStrings = filter.getIdList().stream().map(Object::toString).collect(Collectors.toList());
            conditions.add(String.format("(%s IN (%s))",
                SqlHasIdConverter.COL_ID,
                String.join(",", idStrings)
            ));
        }

        if (filter.getChangedSince() != null) {
            var dateString = SqlHelper.getToDate(this.sqlDialect, filter.getChangedSince());
            conditions.add(String.format("(%s >= %s OR %s >= %s)",
                COL_CREATED_AT,
                dateString,
                COL_MODIFIED_AT,
                dateString
            ));
        }

        if (conditions.size() > 0) {
            return " WHERE " + String.join(" AND ", conditions);
        } else {
            return "";
        }
    }
}

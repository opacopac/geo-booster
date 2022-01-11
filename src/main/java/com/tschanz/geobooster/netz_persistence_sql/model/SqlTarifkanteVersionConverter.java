package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlVersionConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlTarifkanteVersionConverter implements SqlResultsetConverter<TarifkanteVersion>, SqlJsonAggConverter<TarifkanteVersion> {
    private final static String COL_CREATED_AT = "CREATED_AT";
    private final static String COL_MODIFIED_AT = "MODIFIED_AT";

    private final ReadFilter readFilter;
    private final SqlDialect sqlDialect;


    @Override
    public String getTable() {
        return "N_TARIFKANTE_V";
    }


    @Override
    public String[] getFields() {
        return SqlVersionConverter.SELECT_COLS_W_TERM_PER;
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s %s",
            String.join(",", this.getFields()),
            this.getTable(),
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
            SqlVersionConverter.getTerminiertPer(row),
            Collections.emptyList()
        );
    }



    @Override
    public TarifkanteVersion fromJsonAgg(JsonReader reader) {
        return new TarifkanteVersion(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            SqlVersionConverter.getElementIdFromJsonAgg(reader),
            SqlVersionConverter.getGueltigVonFromJsonAgg(reader),
            SqlVersionConverter.getGueltigBisFromJsonAgg(reader),
            SqlHelper.parseLocalDateOrNullfromJsonAgg(reader),
            Collections.emptyList()
        );
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

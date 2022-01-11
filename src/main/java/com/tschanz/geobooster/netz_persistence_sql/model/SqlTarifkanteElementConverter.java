package com.tschanz.geobooster.netz_persistence_sql.model;

import com.google.gson.stream.JsonReader;
import com.tschanz.geobooster.netz.model.Tarifkante;
import com.tschanz.geobooster.netz_persistence.model.ReadFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import com.tschanz.geobooster.persistence_sql.model.SqlJsonAggConverter;
import com.tschanz.geobooster.persistence_sql.model.SqlResultsetConverter;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlHasIdConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlTarifkanteElementConverter implements SqlResultsetConverter<Tarifkante>, SqlJsonAggConverter<Tarifkante> {
    private final static String COL_HST1 = "ID_HS_ELEMENT_1";
    private final static String COL_HST2 = "ID_HS_ELEMENT_2";
    private final static String COL_CREATED_AT = "CREATED_AT";
    private final static String COL_MODIFIED_AT = "MODIFIED_AT";

    private final ReadFilter readFilter;
    private final SqlDialect sqlDialect;


    @Override
    public String getTable() {
        return "N_TARIFKANTE_E";
    }


    @Override
    public String[] getFields() {
        return new String[]{
            SqlHasIdConverter.COL_ID,
            COL_HST1,
            COL_HST2
        };
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
    public Tarifkante fromResultSet(ResultSet row) {
        return new Tarifkante(
            SqlHasIdConverter.getId(row),
            row.getLong(COL_HST1),
            row.getLong(COL_HST2)
        );
    }


    @Override
    @SneakyThrows
    public Tarifkante fromJsonAgg(JsonReader reader) {
        return new Tarifkante(
            SqlHasIdConverter.getIdFromJsonAgg(reader),
            reader.nextLong(),
            reader.nextLong()
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

package com.tschanz.geobooster.versioning_persistence_sql.model;

import com.tschanz.geobooster.persistence_sql.model.SqlDialect;
import com.tschanz.geobooster.persistence_sql.model.SqlGenericResultsetMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class SqlModifiedIdsMapping implements SqlGenericResultsetMapping<Long> {
    public final static String COL_CREATED_AT = "CREATED_AT";
    public final static String COL_MODIFIED_AT = "MODIFIED_AT";
    public final static String[] MODIFIED_INDICATOR_COLS = new String[] { COL_CREATED_AT, COL_MODIFIED_AT };
    private final String table;
    private final SqlDialect sqlDialect;
    private final LocalDateTime changedSince;


    public String getTable() {
        return this.table;
    }


    public String[] getFields() {
        return new String[] { SqlHasIdMapping.COL_ID };
    }


    @Override
    public String getSelectQuery() {
        return String.format(
            "SELECT %s FROM %s WHERE %s",
            String.join(",", this.getFields()),
            this.getTable(),
            this.getWhereClause()
        );
    }


    @Override
    @SneakyThrows
    public Long fromResultSet(ResultSet row) {
        return SqlHasIdMapping.getId(row);
    }


    private String getWhereClause() {
        var dateString = SqlHelper.getToDate(this.sqlDialect, this.changedSince);
        return Arrays.stream(MODIFIED_INDICATOR_COLS)
            .map(col -> String.format("%s >= %s", col, dateString))
            .collect(Collectors.joining(" OR "));
    }
}

package com.tschanz.geobooster.persistence_sql.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;


@Getter
@RequiredArgsConstructor
public class SqlLongFilter implements SqlFilter<Long> {
    private final String column;
    private final Collection<Long> filterValues;


    public static Collection<SqlLongFilter> createSingleton(String column, Collection<Long> filterValues) {
        var filter = new SqlLongFilter(column, filterValues);

        return Collections.singleton(filter);
    }


    @Override
    public String escapeValue(Long value) {
        return value.toString();
    }
}

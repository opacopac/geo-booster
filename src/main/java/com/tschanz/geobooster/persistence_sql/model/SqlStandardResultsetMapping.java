package com.tschanz.geobooster.persistence_sql.model;

import java.sql.ResultSet;
import java.util.Collection;


public interface SqlStandardResultsetMapping<T, F extends SqlFilter<K>, K> {
    String getTable();

    String[] getSelectFields();

    Collection<F> getFilters();

    T fromResultSet(ResultSet row);
}

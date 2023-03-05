package com.tschanz.geobooster.persistence_sql.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.Collection;


public interface SqlStandardResultsetMapping<T, F extends SqlFilter<K>, K> extends RowMapper<T> {
    String getTable();

    String[] getSelectFields();

    Collection<F> getFilters();

    T mapRow(ResultSet row, int rowNum);
}

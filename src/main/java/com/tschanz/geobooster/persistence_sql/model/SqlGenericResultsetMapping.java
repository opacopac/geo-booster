package com.tschanz.geobooster.persistence_sql.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;


public interface SqlGenericResultsetMapping<T> extends RowMapper<T> {
    String getSelectQuery();

    T mapRow(ResultSet row, int rowNumber);
}

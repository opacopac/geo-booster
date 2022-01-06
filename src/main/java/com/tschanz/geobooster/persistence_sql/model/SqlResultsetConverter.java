package com.tschanz.geobooster.persistence_sql.model;

import java.sql.ResultSet;


public interface SqlResultsetConverter<T> {
    String getSelectQuery();

    T fromResultSet(ResultSet row);
}

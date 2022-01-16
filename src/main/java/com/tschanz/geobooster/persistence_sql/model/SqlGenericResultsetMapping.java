package com.tschanz.geobooster.persistence_sql.model;

import java.sql.ResultSet;


public interface SqlGenericResultsetMapping<T> {
    String getSelectQuery();

    T fromResultSet(ResultSet row);
}

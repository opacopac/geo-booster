package com.tschanz.geobooster.netz_persistence_sql.model;

import java.sql.ResultSet;


public interface SqlResultsetConverter<T> {
    T fromResultSet(ResultSet row);
}

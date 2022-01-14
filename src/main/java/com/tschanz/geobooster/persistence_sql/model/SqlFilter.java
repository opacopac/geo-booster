package com.tschanz.geobooster.persistence_sql.model;

import java.util.Collection;


public interface SqlFilter<T> {
    String getColumn();

    Collection<T> getFilterValues();

    String escapeValue(T value);
}

package com.tschanz.geobooster.persistence_sql.model;

import com.google.gson.stream.JsonReader;

import java.util.Collection;


public interface SqlStandardJsonAggMapping<T, F extends SqlFilter<K>, K> {
    String getTable();

    String[] getSelectFields();

    Collection<F> getFilters();

    T fromJsonAgg(JsonReader reader);
}

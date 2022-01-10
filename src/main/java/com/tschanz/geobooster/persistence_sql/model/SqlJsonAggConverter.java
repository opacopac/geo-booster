package com.tschanz.geobooster.persistence_sql.model;

import com.google.gson.stream.JsonReader;


public interface SqlJsonAggConverter<T> {
    String getTable();

    String[] getFields();

    T fromJsonAgg(JsonReader reader);
}

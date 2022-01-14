package com.tschanz.geobooster.persistence_sql.model;


public interface SqlStandardConverter<T, F extends SqlFilter<K>, K> extends SqlStandardJsonAggConverter<T, F, K>, SqlStandardResultsetConverter<T, F, K> {
}

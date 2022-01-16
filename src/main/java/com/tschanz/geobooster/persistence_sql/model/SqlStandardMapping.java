package com.tschanz.geobooster.persistence_sql.model;


public interface SqlStandardMapping<T, F extends SqlFilter<K>, K> extends SqlStandardJsonAggMapping<T, F, K>, SqlStandardResultsetMapping<T, F, K> {
}

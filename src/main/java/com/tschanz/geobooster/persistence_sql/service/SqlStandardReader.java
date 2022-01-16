package com.tschanz.geobooster.persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.model.SqlFilter;
import com.tschanz.geobooster.persistence_sql.model.SqlStandardMapping;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class SqlStandardReader {
    private final SqlStandardResultsetReader resultsetReader;
    private final SqlStandardJsonAggReader jsonAggReader;
    private final ConnectionState connectionState;



    @SneakyThrows
    public <T, F extends SqlFilter<K>, K> List<T> read(SqlStandardMapping<T, F, K> mapping) {
        if (this.connectionState.isUseJsonAgg()) {
            return this.jsonAggReader.read(mapping);
        } else {
            return this.resultsetReader.read(mapping);
        }
    }
}

package com.tschanz.geobooster.versioning_persistence_sql.service;

import com.tschanz.geobooster.persistence_sql.model.ConnectionState;
import com.tschanz.geobooster.persistence_sql.model.SqlRowCountMapping;
import com.tschanz.geobooster.persistence_sql.service.SqlGenericResultsetReader;
import com.tschanz.geobooster.persistence_sql.service.SqlStandardReader;
import com.tschanz.geobooster.util.model.DoubleList;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlAllIdsMapping;
import com.tschanz.geobooster.versioning_persistence_sql.model.SqlModifiedIdsMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class SqlChangeDetector {
    private final ConnectionState connectionState;
    private final SqlStandardReader standardReader;
    private final SqlGenericResultsetReader genericReader;


    public DoubleList<Long> findModifiedDeletedIds(String table, LocalDateTime changedSince, Collection<Long> currentIds) {
        var modifiedIds = this.findModifiedIds(table, changedSince);
        var rowCount = this.readRowCount(table);

        List<Long> removedIds = Collections.emptyList();
        if (currentIds.size() + modifiedIds.size() > rowCount) {
            removedIds = this.findRemovedIds(table, currentIds);
        }

        return new DoubleList<>(
            modifiedIds,
            removedIds
        );
    }


    private List<Long> findModifiedIds(String table, LocalDateTime changedSince) {
        var mapping = new SqlModifiedIdsMapping(
            table,
            this.connectionState.getSqlDialect(),
            changedSince
        );

        return this.genericReader.read(mapping);
    }


    private Long readRowCount(String table) {
        var mapping = new SqlRowCountMapping(table);

        return this.genericReader.read(mapping).get(0);
    }


    private List<Long> findRemovedIds(String table, Collection<Long> currentIds) {
        var mapping = new SqlAllIdsMapping(table);
        var allIds = this.standardReader.read(mapping);
        var allIdsMap = new HashMap<Long, Integer>();
        allIds.forEach(id -> allIdsMap.put(id, 0));

        return currentIds.stream()
            .filter(vid -> !allIdsMap.containsKey(vid))
            .collect(Collectors.toList());
    }
}

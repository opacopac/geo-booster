package com.tschanz.geobooster.versioning.service;

import com.tschanz.geobooster.util.service.DateHelper;
import com.tschanz.geobooster.versioning.model.HasId;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning.model.Version;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class VersioningHelper {
    public static <T extends HasId> Map<Long, T> createIdMap(Collection<T> hasIdObjects) {
        var idMap = new HashMap<Long, T>();
        hasIdObjects.forEach(obj -> idMap.put(obj.getId(), obj));

        return idMap;
    }


    public static <T extends Version> Map<Long, Collection<T>> createElementIdMap(Collection<T> versions) {
        var elementIdMap = new HashMap<Long, Collection<T>>();
        versions.forEach(v -> {
            var elementVersions = elementIdMap.computeIfAbsent(v.getElementId(), k -> new ArrayList<>());
            elementVersions.add(v);
        });

        return elementIdMap;
    }


    public static <T extends Version> boolean isVersionInTimespan(T version, LocalDate date) {
        return DateHelper.isInTimespan(date, version.getGueltigVon(), version.getGueltigBis());
    }


    public static <T extends Version> Collection<T> filterVersions(Collection<T> versions, LocalDate date) {
        return versions.stream()
            .filter(v -> isVersionInTimespan(v, date))
            .collect(Collectors.toList());
    }


    public static <T extends Version> T filterSingleVersion(Collection<T> versions, LocalDate date, Pflegestatus minStatus) {
        return versions.stream()
            .filter(v -> isVersionInTimespan(v, date))
            .filter(v -> v.getPflegestatus().getSortOrder() >= minStatus.getSortOrder())
            .min(Comparator.comparingInt(v -> v.getPflegestatus().getSortOrder())) // return version with lowest pflegestatus
            .orElse(null);
    }
}

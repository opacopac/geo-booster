package com.tschanz.geobooster.versioning_repo.model;

import com.tschanz.geobooster.util.service.DateHelper;
import com.tschanz.geobooster.versioning.model.Element;
import com.tschanz.geobooster.versioning.model.Version;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class VersionedObjectMap<E extends Element, V extends Version> {
    private final Map<Long, E> elementMap = new HashMap<>();
    private final Map<Long, V> versionMap = new HashMap<>();
    private final Map<Long, List<Long>> elementVersionIds = new HashMap<>();


    public VersionedObjectMap(Collection<E> elements, Collection<V> versions) {
        elements.forEach(this::putElement);
        versions.forEach(this::putVersion);
    }


    public void putElement(E element) {
        this.elementMap.put(element.getId(), element);
    }


    public void putVersion(V version) {
        this.versionMap.put(version.getId(), version);
        var evIds = this.elementVersionIds.computeIfAbsent(version.getElementId(), k -> new ArrayList<>());
        evIds.add(version.getId());
    }


    public void deleteElement(long id) {
        this.elementMap.remove(id);
    }


    public void deleteVersion(long id) {
        var elementId = this.versionMap.get(id).getElementId();
        var evIds = this.elementVersionIds.get(elementId);
        evIds.remove(id);
        this.versionMap.remove(id);
    }


    public Collection<E> getAllElements() {
        return this.elementMap.values();
    }


    public Collection<V> getAllVersions() {
        return this.versionMap.values();
    }


    public E getElement(long id) {
        return this.elementMap.get(id);
    }


    public V getVersion(long id) {
        return this.versionMap.get(id);
    }


    public List<V> getElementVersions(long elementId) {
        return this.elementVersionIds.get(elementId)
            .stream()
            .map(this::getVersion)
            .collect(Collectors.toList());
    }


    public V getElementVersionAtDate(long elementId, LocalDate date) {
        return this.getElementVersions(elementId)
            .stream()
            .filter(version -> DateHelper.isInTimespan(date, version.getGueltigVon(), version.getGueltigBis()))
            .findFirst()
            .orElse(null);
    }
}

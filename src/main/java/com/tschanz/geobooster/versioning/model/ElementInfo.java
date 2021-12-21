package com.tschanz.geobooster.versioning.model;

import com.tschanz.geobooster.common.service.DateHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class ElementInfo<T, K extends Version<T>> {
    private final long id;
    private final List<K> versions = new ArrayList<>();


    public K getVersion(LocalDate date) {
        return this.versions
            .stream()
            .filter(version -> DateHelper.isInTimespan(
                date,
                version.getVersionInfo().getGueltigVon(),
                version.getVersionInfo().getGueltigBis()
            ))
            .findFirst()
            .orElse(null);
    }
}

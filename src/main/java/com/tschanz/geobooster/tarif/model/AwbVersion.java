package com.tschanz.geobooster.tarif.model;

import com.tschanz.geobooster.versioning.model.HasPflegestatus;
import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class AwbVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final Pflegestatus pflegestatus;
    private final Collection<Long> includeVkaIds;
    private final Collection<Long> excludeVkaIds;
    private final Collection<Long> includeTkIds;
    private final Collection<Long> excludeTkIds;
    private final Collection<Long> includeZonenplanIds;
    private final Collection<Long> includeRgaIds;
}

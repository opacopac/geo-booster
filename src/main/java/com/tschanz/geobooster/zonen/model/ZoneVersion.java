package com.tschanz.geobooster.zonen.model;

import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class ZoneVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final long ursprungsZoneId;
    private final Collection<Long> verkehrskantenIds;
}

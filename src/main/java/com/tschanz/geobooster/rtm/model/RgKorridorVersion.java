package com.tschanz.geobooster.rtm.model;

import com.tschanz.geobooster.versioning.model.Pflegestatus;
import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class RgKorridorVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final Pflegestatus pflegestatus;
    private final Collection<Long> tarifkantenIds;
}

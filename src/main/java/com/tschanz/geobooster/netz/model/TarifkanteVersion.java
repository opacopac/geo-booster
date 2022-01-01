package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class TarifkanteVersion implements Version {
    private final long id;
    private final long elementId;
    private final LocalDate gueltigVon;
    private final LocalDate gueltigBis;
    private final LocalDate terminiertPer;
    private final List<Long> verkehrskanteIds;
}

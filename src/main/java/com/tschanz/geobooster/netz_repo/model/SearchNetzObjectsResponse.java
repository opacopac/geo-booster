package com.tschanz.geobooster.netz_repo.model;

import com.tschanz.geobooster.netz.model.HaltestelleVersion;
import com.tschanz.geobooster.netz.model.TarifkanteVersion;
import com.tschanz.geobooster.netz.model.VerkehrskanteVersion;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;


@Getter
@RequiredArgsConstructor
public class SearchNetzObjectsResponse {
    private final Collection<HaltestelleVersion> haltestelleVersions;
    private final Collection<VerkehrskanteVersion> verkehrskanteVersions;
    private final Collection<TarifkanteVersion> tarifkanteVersions;
}

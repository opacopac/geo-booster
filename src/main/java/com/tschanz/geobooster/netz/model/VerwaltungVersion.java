package com.tschanz.geobooster.netz.model;

import com.tschanz.geobooster.versioning.model.Version;
import com.tschanz.geobooster.versioning.model.VersionInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class VerwaltungVersion implements Version<Verwaltung> {
    private final VersionInfo<Verwaltung> versionInfo;
}

package com.tschanz.geobooster.rtm_repo.service;

import com.tschanz.geobooster.rtm.model.RgKorridor;
import com.tschanz.geobooster.rtm.model.RgKorridorVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;

import java.time.LocalDate;
import java.util.Collection;


public interface RgKorridorRepo extends VersionedObjectRepo<RgKorridor, RgKorridorVersion> {
    Collection<RgKorridor> getElementsByRgId(long relationsgebietId);

    Collection<RgKorridorVersion> getVersionsByRgId(long relationsgebietId, LocalDate date);
}

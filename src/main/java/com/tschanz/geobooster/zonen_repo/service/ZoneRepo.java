package com.tschanz.geobooster.zonen_repo.service;

import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;
import com.tschanz.geobooster.zonen.model.Zone;
import com.tschanz.geobooster.zonen.model.ZoneVersion;

import java.time.LocalDate;
import java.util.Collection;


public interface ZoneRepo extends VersionedObjectRepo<Zone, ZoneVersion> {
    Collection<Zone> getElementsByZonenplanId(long zonenplanId);

    Collection<ZoneVersion> getVersionsByZonenplanId(long zonenplanId, LocalDate date);
}

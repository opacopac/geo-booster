package com.tschanz.geobooster.netz_repo.service;

import com.tschanz.geobooster.netz.model.Betreiber;
import com.tschanz.geobooster.netz.model.BetreiberVersion;
import com.tschanz.geobooster.versioning_repo.service.VersionedObjectRepo;


public interface BetreiberRepo extends VersionedObjectRepo<Betreiber, BetreiberVersion> {
}
